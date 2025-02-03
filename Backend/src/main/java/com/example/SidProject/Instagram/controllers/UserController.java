package com.example.SidProject.Instagram.controllers;

import com.example.SidProject.Instagram.DTO.*;
import com.example.SidProject.Instagram.Services.UserService;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
import com.example.SidProject.Instagram.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    @PostMapping("/users")
    String addUser(@ModelAttribute UserDTO userDTO) {
        userService.addUser(userDTO);
        return "";
    }
    @GetMapping("/users/{userId}")
    User getUser(@PathVariable String userId) {
        return userService.getUser(userId).get();
    }

    @PostMapping("/users/login")
    String login(@RequestBody LoginCredentials loginCredentials) {
        User user = userRepository.getUserByName(loginCredentials.getUsername());
        if(user == null){
            return "failed";
        }
        return user.getId();
    }

    @GetMapping("/users/{userId}/details")
    public UserProfileDTO getUserDetails(@PathVariable String userId) {
        return userService.getUserProfile(userId);
    }
}
