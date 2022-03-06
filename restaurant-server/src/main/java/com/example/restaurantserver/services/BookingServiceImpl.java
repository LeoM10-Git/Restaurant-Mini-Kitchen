package com.example.restaurantserver.services;

import com.example.restaurantserver.models.BookingData;
import com.example.restaurantserver.repos.BookingRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{
    private final BookingRepo bookingRepo;
    private final MongoTemplate mongoTemplate;
    private final BookingSessionService bookingSessionService;

    @Override
    public BookingData getBookingById(String bookingId) {
        Query query = new Query(Criteria.where("bookingId").is(bookingId));
        return mongoTemplate.findOne(query, BookingData.class);
    }

    @Override
    public BookingData saveBooking(BookingData booking) {
        booking.setBookingId(UUID.randomUUID().toString().substring(0, 8));
        String bookingDate = booking.getDate();
        String sessionTime = booking.getSession();
        bookingSessionService.updateBookingSession(bookingDate, sessionTime);
        return bookingRepo.save(booking);
    }

    @Override
    public List<BookingData> getAllBookingsByAccount(String email) {
        Query query = new Query(Criteria.where("accountOwnerEmail").is(email.toLowerCase()));
        return  mongoTemplate.find(query, BookingData.class);
    }

    @Override
    public List<BookingData> getAllBookings() {
        return mongoTemplate.findAll(BookingData.class);
    }

    @Override
    public BookingData updateBooking(BookingData bookingData) {
        /*for update, use mongoTemplate*/
        Query query = new Query(Criteria.where("bookingId").is(bookingData.getBookingId()));
        BookingData bookingToUpdate = mongoTemplate.findOne(query, BookingData.class);
        if (bookingToUpdate != null) {
            bookingToUpdate.setName(bookingData.getName());
            bookingToUpdate.setGuestNumber(bookingToUpdate.getGuestNumber());
            bookingToUpdate.setAdultNumber(bookingData.getAdultNumber());
            bookingToUpdate.setChildrenNumber(bookingData.getChildrenNumber());
            bookingToUpdate.setEmail(bookingData.getEmail());
            bookingToUpdate.setDate(bookingData.getDate());
            bookingToUpdate.setSession(bookingData.getSession());
            bookingToUpdate.setPhoneNumber(bookingData.getPhoneNumber());
            return mongoTemplate.save(bookingToUpdate);
        }else return bookingData;
    }

    /*Delete booking*/
    @Override
    public void deleteBooking(String bookingId) {
        Query query = new Query(Criteria.where("bookingId").is(bookingId));
        BookingData bookingToDelete = getBookingById(bookingId);
        if (bookingToDelete != null) {
            mongoTemplate.remove(query, BookingData.class);
            log.info("Delete success");
        } else throw new RuntimeException("Booking to delete not found");
    }

}
