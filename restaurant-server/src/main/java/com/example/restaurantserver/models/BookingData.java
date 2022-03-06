package com.example.restaurantserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingData {

    @Id
    private String _id;
    private String accountOwnerEmail;
    private String bookingId;
    private String name;
    private String email;
    private String phoneNumber;
    private int guestNumber;
    private int adultNumber;
    private int childrenNumber;
    private String date;
    private String session;
    private int googleRangeId;
}
