package com.example.restaurantserver.controllers;


import com.example.restaurantserver.models.*;
import com.example.restaurantserver.services.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResourceController {
    private final UserService userService;
    private final BookingService bookingService;
    private final GoogleSheetsServiceImpl googleSheetsService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final BookingSessionService bookingSessionService;

    @Value("${spring.mail.username}")
    String emailSentFrom;
    @Value("${restaurant.email}")
    String restaurantEmail;

    @GetMapping("/user/resources/user-account")
    public ResponseEntity<UserData> getUserAccountInfo(Principal principal) {
        String email = principal.getName();
        UserData user = userService.getUserByEmail(email);
        user.setPassword("");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    /*Deal with the booking data*/
    @PostMapping("/user/resources/bookings")
    public ResponseEntity<BookingData> saveBookings(@RequestBody  BookingData bookingData, Principal principal)
            throws NoSuchFieldException, IllegalAccessException, GeneralSecurityException, IOException {

        /*save to google sheet*/
        /*In case of server down sometime, when server back , and post new booking, get the latest saved rangeIndex,
         plus to add the new booking*/
        List<BookingData> bookingDataList = bookingService.getAllBookingsByAccount(principal.getName());
        int rangeIndex;
        if (bookingDataList.size() <= 0) {
            rangeIndex = 2;
        } else {
            rangeIndex = bookingDataList.get(bookingDataList.size() - 1).getGoogleRangeId() + 1;
        }
        String range = "A" + rangeIndex;
        bookingData.setGoogleRangeId(rangeIndex);
        bookingData.setAccountOwnerEmail(principal.getName());

        BookingData newBooking = bookingService.saveBooking(bookingData);
        ValueRange body = googleSheetsService.requestBuilder(newBooking, range);
        googleSheetsService.insertRow(body, range);
        /*Save to database with the range index*/
        /*Send confirmation email for table booking*/
        Email email = new Email();
        email.setTo(principal.getName());
        email.setFrom(emailSentFrom);
        email.setSubject("Mini Kitchen-Booking Confirmation");

        Map<String, Object> model = new HashMap<>();
        String username = userService.getUserByEmail(principal.getName()).getName();
        String date = newBooking.getDate();
        String session = newBooking.getSession();
        String phoneNumber = "";
        int guestNumber = 5;
        model.put("name", username);
        model.put("date", date);
        model.put("session", session);
        model.put("phoneNumber", phoneNumber);
        model.put("guestNumber", guestNumber);
        email.setModel(model);
        try {
            emailService.sendEmailWithTemplate(email, "booking-confirmation.ftl");
        } catch(MailSendException mse) {
            log.error("Booking confirmation with email %s not send for exception".formatted(email));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @GetMapping("/user/resources/bookings")
    public ResponseEntity<List<BookingData>> getAllBookingsByAccount(Principal principal)  {
        List<BookingData> allBookings = bookingService
                .getAllBookingsByAccount(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(allBookings);
    }

    @GetMapping("/user/resources/bookings/{bookingId}")
    public ResponseEntity<BookingData> getBookingById(@PathVariable String bookingId) {
        BookingData booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }

    /*update the specified booking by booking id*/
    @PutMapping("/user/resources/bookings/update/{bookingId}")
    public ResponseEntity<BookingData> updateBooking(@RequestBody BookingData bookingData,
                                                     @PathVariable String bookingId,
                                                     Principal principal)
            throws GeneralSecurityException, IOException {
        BookingData savedBooking = bookingService.getBookingById(bookingId);
        BookingData booking = bookingService.updateBooking(bookingData);
        /*if NOT the updating booking's session and date is same as the saved one, then no update the session*/
        if (! (savedBooking.getDate().equals(bookingData.getDate())
                && savedBooking.getSession().equals(bookingData.getDate()))) {
            /*Add back the session, then delete the new session*/
            bookingSessionService.addBackBookingSession(savedBooking.getDate(), savedBooking.getSession());
            bookingSessionService.updateBookingSession(booking.getDate(), booking.getSession());
        }
        /*Get the Google rangeId from database, and update the Google sheet*/
        int rangeId = bookingService.getBookingById(bookingId).getGoogleRangeId();
        bookingData.setAccountOwnerEmail(principal.getName());
        String range = "A" + rangeId;
        ValueRange body = googleSheetsService.requestBuilder(bookingData, range);
        googleSheetsService.insertRow(body, range);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    /*Delete the booking by id*/
    @DeleteMapping("/user/resources/bookings/delete/{bookingId}")
    public ResponseEntity<Map<String, String>> deleteBooking(@PathVariable String bookingId)
            throws GeneralSecurityException, IOException {
        /*Add the available table back to the session*/
        BookingData booking = bookingService.getBookingById(bookingId);
        bookingSessionService.addBackBookingSession(booking.getDate(), booking.getSession());
         /*Delete Google sheet row by range id*/
        int rangeId = bookingService.getBookingById(bookingId).getGoogleRangeId();
        String range = "A" + rangeId + ":" + "J" +rangeId;
        googleSheetsService.deleteRow(range);
        /*delete google sheet row first, then delete the database record*/
        bookingService.deleteBooking(bookingId);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "booking deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /*User contact us, to send email to restaurant*/
    @PostMapping("/user/resources/contact-us")
    public ResponseEntity<Map<String, String>> contact(@RequestBody ContactContent content) {
        Email email = new Email();
        Map<String, Object> model = new HashMap<>();
        email.setTo(restaurantEmail);
        email.setFrom(emailSentFrom);
        email.setSubject("Customer feedback");
        model.put("name", content.getName());
        model.put("email", content.getEmail());
        model.put("phoneNumber", content.getPhoneNumber());
        model.put("message", content.getMessage());

        email.setModel(model);
        emailService.sendEmailWithTemplate(email, "customer-contact.ftl");
        Map<String, String> response = new HashMap<>();
        response.put("message", "message sent");
        return ResponseEntity.ok(response);
    }

    /*User update account information*/
    @PutMapping("/user/resources/update/account-info")
    public ResponseEntity<Map<String, String>> updateUserInfo(@RequestBody UpdateUserInfo updateUserInfo,
                                                              Principal principal) {
        Map<String, String> response = new HashMap<>();
        if (!updateUserInfo.getCurrentPassword().equals("")) {
            String savedPassword = userService.getUserByEmail(principal.getName()).getPassword();
            Long userId = userService.getUserByEmail(principal.getName()).getId();
            String currentPassword = updateUserInfo.getCurrentPassword();
            if (passwordEncoder.matches(currentPassword, savedPassword)) {
                userService.updateUserWithPassword(updateUserInfo, userId, principal.getName());
                response.put("updated", "true");
            }else {
                response.put("updated", "false");
            }
        }else {
            Long userId = userService.getUserByEmail(principal.getName()).getId();
            UserData userData = new UserData();
            userData.setName(updateUserInfo.getName());
            userData.setEmail(updateUserInfo.getEmail().toLowerCase());
            log.info("User email %s updated to %s".formatted(principal.getName(),
                    updateUserInfo.getEmail().toLowerCase()));
            userService.updateUserWithoutPassword(updateUserInfo, userId, principal.getName());
            response.put("updated", "true");
        }
        return ResponseEntity.ok(response);
    }

    /*Deal with booking session*/
    @GetMapping("/user/resources/booking/booking-session")
    public ResponseEntity<List<BookingSession>> getAllAvailableSessions() throws IllegalAccessException {
        return ResponseEntity.ok(this.bookingSessionService.getAllAvailableSessions());
    }

    /*FAQ Page*/
    @GetMapping("/user/FAQs")
    public ResponseEntity<List<Faq>> getFAQs() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Faq> FAQs =  mapper.readValue(new File("src/main/resources/static/FAQ/FAQs.json"),
                new TypeReference<List<Faq>>(){});
        return ResponseEntity.ok(FAQs);
    }

}
