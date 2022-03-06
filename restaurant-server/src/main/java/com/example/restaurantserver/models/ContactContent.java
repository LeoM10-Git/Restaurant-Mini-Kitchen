package com.example.restaurantserver.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactContent {
    private String name;
    private String email;
    private String phoneNumber;
    private String message;
}
