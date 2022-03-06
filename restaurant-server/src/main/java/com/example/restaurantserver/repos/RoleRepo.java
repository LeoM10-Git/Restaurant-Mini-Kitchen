package com.example.restaurantserver.repos;


import com.example.restaurantserver.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo  extends JpaRepository <UserRole, Long> {
    UserRole findByRoleName(String roleName);
}
