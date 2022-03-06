package com.example.restaurantserver.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfo {
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;
}
