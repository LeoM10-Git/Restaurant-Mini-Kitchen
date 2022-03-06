package com.example.restaurantserver.services;


import com.example.restaurantserver.models.UpdateUserInfo;
import com.example.restaurantserver.models.UserData;
import com.example.restaurantserver.models.UserRole;
import com.example.restaurantserver.repos.RoleRepo;
import com.example.restaurantserver.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserData saveUser(UserData user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserData newUser = userRepo.save(user);
        addUserRoleToUser(user.getEmail(), "ROLE_USER");
        return newUser;
    }

    /*Update user information*/
    @Override
    public UserData updateUserWithPassword(UpdateUserInfo user, Long userId) {
        log.info("Updating  user {} to the database", user.getName());
        UserData savedUser = userRepo.getById(userId);
        savedUser.setEmail(user.getEmail());
        savedUser.setName(user.getName());
        savedUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
        return userRepo.save(savedUser);
    }

    @Override
    public UserData updateUserWithoutPassword(UpdateUserInfo user, Long userId) {
        log.info("Updating  user {} to the database without password updating", user.getName());
        UserData savedUser = userRepo.getById(userId);
        savedUser.setEmail(user.getEmail());
        savedUser.setName(user.getName());
        return userRepo.save(savedUser);
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        log.info("Saving new UserRole " +
                "{} to the database", userRole.getRoleName());
         roleRepo.save(userRole);
    }

    @Override
    public void addUserRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user {}", roleName, email);
        UserData user = getUserByEmail(email);
        UserRole role = roleRepo.findByRoleName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public UserData getUserByEmail(String email) {
        if (userRepo.findByEmail(email).isEmpty()) {
            log.error("User with email {} not found", email);
            return null;
        }else {
            log.info("Fetching user {} ", email);
            return userRepo.findByEmail(email).get();
        }
    }

    @Override
    public List<UserData> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserData> user = userRepo.findByEmail(username);
        if (user.isEmpty()) {
            log.error("User {} not found", username);
            throw new UsernameNotFoundException("User not found in the database");
        }else {
            log.info("User {} found in the database", username);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.get().getRoles().forEach(UserRole -> authorities.add(new SimpleGrantedAuthority(UserRole.getRoleName())));
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                   user.get().getPassword(),
                    authorities);
        }
    }
}
