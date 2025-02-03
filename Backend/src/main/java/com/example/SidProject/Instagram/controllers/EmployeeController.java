package com.example.SidProject.Instagram.controllers;

import com.example.SidProject.Instagram.DTO.PostResponse;
import com.example.SidProject.Instagram.Services.EmployeeService;
import com.example.SidProject.Instagram.Services.PostService;
import com.example.SidProject.Instagram.model.Employee;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    private UserRepository userRepository;
    private EmployeeService employeeService;
    private PostService postService;
    @GetMapping(path = "/employee/{id}")
    public Employee getEmployees(@PathVariable Integer id)  {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping(path = "/user/{user_id}/post")
    public String postPhoto(@PathVariable String user_id,
                            @RequestParam("file")MultipartFile file,
                            @RequestParam("caption") String caption)  {
        User user = userRepository.findById(user_id).get();
        return postService.uploadPost(user, file, caption);
    }

    @GetMapping(path = "/feed/{userId}")
    public List<PostResponse> getAllPosts(@PathVariable String userId) {

        return postService.getAllPosts(userId);
    }

    @GetMapping(path = "/posts/{userId}")
    public List<PostResponse> getAllPostsOfUser(@PathVariable String userId) {

        return postService.getAllPostsOfUser(userId);
    }
}
