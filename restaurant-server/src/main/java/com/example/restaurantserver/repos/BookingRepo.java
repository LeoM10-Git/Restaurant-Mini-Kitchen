package com.example.restaurantserver.repos;

import com.example.restaurantserver.models.BookingData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepo extends MongoRepository<BookingData, String> {
    Optional<BookingData> findBookingByBookingId(String bookingId);
}
