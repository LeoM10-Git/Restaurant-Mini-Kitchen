package com.example.restaurantserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String to;
    private String from;
    private String subject;
    private String content;
    private Map< String, Object > model;
}
