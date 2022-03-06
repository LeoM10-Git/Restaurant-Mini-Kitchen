package com.example.restaurantserver.services;

import com.example.restaurantserver.models.BookingSession;
import com.example.restaurantserver.models.SessionInfo;

import java.util.Date;
import java.util.List;

public interface BookingSessionService {

    void setBookingSession(BookingSession bookingSession);
    void updateBookingSession(String date, String sessionId);
    List<BookingSession> getAllAvailableSessions() throws IllegalAccessException;
    void addBackBookingSession(String date, String sessionTime);
}
