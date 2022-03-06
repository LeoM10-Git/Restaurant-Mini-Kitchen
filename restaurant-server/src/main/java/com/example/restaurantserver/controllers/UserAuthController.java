package com.example.restaurantserver.controllers;

import com.example.restaurantserver.models.Email;
import com.example.restaurantserver.models.UserData;
import com.example.restaurantserver.services.AuthService;
import com.example.restaurantserver.services.EmailService;
import com.example.restaurantserver.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserAuthController {
    private final UserService userService;
    private final AuthService authService;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    String emailSentFrom;

    public UserAuthController(UserService userService, AuthService authService, EmailService emailService) {
        this.userService = userService;
        this.authService = authService;
        this.emailService = emailService;
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserData>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserData user) throws Exception{

        Map<String, String> map = new HashMap<String, String>();
        /*Check if user already exits*/
        user.setEmail(user.getEmail().toLowerCase());
        UserData savedUserData = userService.getUserByEmail(user.getEmail());
        log.info("Register user" + user.getEmail());
        if ( savedUserData != null ) {
            log.info("User exits, not registered");
            map.put("message", "failed");
        } else {
            /*Send confirmation email for account registered*/
            Email email = new Email();
            email.setTo(user.getEmail());
            email.setFrom(emailSentFrom);
            email.setSubject("Welcome to register with Mini Kitchen");

            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            email.setModel(model);
            boolean isEmail = true;
            /*Try to send email, if email not valid, catch exception, and return the not registered message*/
            try{
                emailService.sendEmailWithTemplate(email, "register-confirmation.ftl");
            }catch(MailSendException mse) {
                isEmail = false;
                log.error(mse.getMessage());
                log.info("User email %s is not valid email address, not registered".formatted(user.getEmail()));
                map.put("message", "invalid");
            }
            if (isEmail) {
                log.info("User %s registered".formatted(user.getEmail()));
                userService.saveUser(user);
                map.put("message", "registered");
            }
        }
        return ResponseEntity.ok(map);
    }



    /*---------------------------------------------*/
    /*Only accessible when user successfully login*/
    /*---------------------------------------------*/
    /*If user choose remember me, provide a long time expired token*/
    @GetMapping("/user/remember-me")
    public void getRememberMeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                /*provide 30 days for remember me*/
                authService.getAuthToken(request, response, authorizationHeader, 60 * 24 * 30);
            }catch(Exception e){
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }

    @GetMapping("user/auth/verify")
    public ResponseEntity<String> verifyAuth(){
        return ResponseEntity.ok("Verified");
    }

    /*When user short term access token expired, call this path to get new access token by refresh token*/
    @GetMapping("/user/get-refresh-token")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                log.info("Refreshed access token provided");
                authService.getAuthToken(request, response, authorizationHeader, 20);
            }catch(Exception e){
                Map<String, String> error = new HashMap<String, String>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else {
            log.error("Refresh token is missing, not get access token refreshed");
        }
    }
}
