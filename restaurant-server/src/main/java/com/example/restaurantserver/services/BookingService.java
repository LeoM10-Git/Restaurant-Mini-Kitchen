package com.example.restaurantserver.services;

import com.example.restaurantserver.models.BookingData;

import java.util.List;

public interface BookingService {
    BookingData getBookingById(String bookingId);
    BookingData saveBooking(BookingData booking) throws NoSuchFieldException, IllegalAccessException;
    List<BookingData> getAllBookingsByAccount(String email);
    BookingData updateBooking(BookingData bookingData);
    void deleteBooking(String bookingId);
    List<BookingData> getAllBookings();
}
