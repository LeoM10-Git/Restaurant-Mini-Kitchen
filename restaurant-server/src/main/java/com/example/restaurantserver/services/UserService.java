package com.example.restaurantserver.services;


import com.example.restaurantserver.models.UpdateUserInfo;
import com.example.restaurantserver.models.UserData;
import com.example.restaurantserver.models.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserData saveUser(UserData user);

    void saveUserRole(UserRole userRole);

    void addUserRoleToUser(String email, String userRoleName);

    UserData getUserByEmail(String username);

    List<UserData> getUsers();

    UserData updateUserWithoutPassword(UpdateUserInfo updateUserInfo, Long userId, String oldEmail);

    UserData updateUserWithPassword(UpdateUserInfo updateUserInfo, Long userId, String oldEmail);
}
