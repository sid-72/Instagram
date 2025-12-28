package com.example.SidProject.Instagram.repositories;

import com.example.SidProject.Instagram.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> getUserByName(String name);
}
