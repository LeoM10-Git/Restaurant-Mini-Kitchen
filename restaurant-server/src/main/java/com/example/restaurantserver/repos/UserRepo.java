package com.example.restaurantserver.repos;

import com.example.restaurantserver.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);
}
