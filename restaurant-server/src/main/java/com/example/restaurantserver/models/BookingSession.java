package com.example.restaurantserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingSession {
    @Id
    private String _id;
    private String date;
    private List<SessionInfo> sessions;

}
