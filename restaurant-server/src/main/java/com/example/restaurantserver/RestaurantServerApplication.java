package com.example.restaurantserver;

import com.example.restaurantserver.models.*;
import com.example.restaurantserver.services.BookingService;
import com.example.restaurantserver.services.BookingSessionService;
import com.example.restaurantserver.services.UserService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@SpringBootApplication
public class RestaurantServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServerApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }

}
