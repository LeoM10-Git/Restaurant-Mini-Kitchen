package com.example.restaurantserver.services;

import com.example.restaurantserver.models.BookingSession;
import com.example.restaurantserver.models.SessionInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class BookingSessionServiceImpl implements BookingSessionService {
    private final MongoTemplate mongoTemplate;

    @Override
    public void setBookingSession(BookingSession bookingSession) {
        mongoTemplate.save(bookingSession);
    }

    @Override
    public void updateBookingSession(String date, String sessionTime) {
        Query query = new Query(Criteria.where("date").is(date));
        BookingSession bookingSession = mongoTemplate.findOne(query, BookingSession.class);
        if (bookingSession != null) {
                List<SessionInfo> updatedSessionInfo = new ArrayList<SessionInfo>();
                for (SessionInfo session: bookingSession.getSessions()) {
                    if (sessionTime.equals(session.getTime())) {
                        session.setAvailableTables(session.getAvailableTables() - 1);
                    }
                    updatedSessionInfo.add(session);
                }
                bookingSession.setSessions(updatedSessionInfo);
            log.info("%s: session: %s deleted 1".formatted(date, sessionTime) );
            mongoTemplate.save(bookingSession);
        } else log.info("Booking session not found");
    }

    @Override
    public List<BookingSession> getAllAvailableSessions() {
        return mongoTemplate.findAll(BookingSession.class);
    }

    @Override
    public void addBackBookingSession(String date, String sessionTime) {
        Query query = new Query(Criteria.where("date").is(date));
        BookingSession bookingSession = mongoTemplate.findOne(query, BookingSession.class);
        if (bookingSession != null) {
            List<SessionInfo> updatedSessionInfo = new ArrayList<SessionInfo>();
            for (SessionInfo session: bookingSession.getSessions()) {
                if (sessionTime.equals(session.getTime())) {
                    session.setAvailableTables(session.getAvailableTables() + 1);
                }
                updatedSessionInfo.add(session);
            }
            bookingSession.setSessions(updatedSessionInfo);
            log.info("%s: session: %s added 1".formatted(date, sessionTime) );
            mongoTemplate.save(bookingSession);
        } else log.error("Booking session not found");
    }
}

