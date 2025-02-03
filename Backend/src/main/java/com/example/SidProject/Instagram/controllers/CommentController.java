package com.example.SidProject.Instagram.controllers;

import com.example.SidProject.Instagram.Services.CommentService;
import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
import com.example.SidProject.Instagram.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    private CommentService commentService;
    private PostRepository postRepository;
    private UserRepository userRepository;


    @PostMapping("/comments")
    CommentDTO addComment(@RequestBody CommentDTO commentDTO) {
        String postId = commentDTO.getPostId();
        System.out.println(commentDTO.toString());
        if(postId == null) {
            throw new RuntimeException("Post id is required");
        }
        String userId = commentDTO.getUserId();
        Post post = null;
        User user = null;
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(postOptional.isPresent()) {
            post = postOptional.get();
        } else {
            throw new RuntimeException("Post not found");
        }
        if(userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            throw new RuntimeException("User not found");
        }
        return commentService.addComment(commentDTO, post, user);
    }

    @GetMapping("/comments/{commentId}")
    CommentDTO getComment(@PathVariable String commentId) {
        return commentService.getComment(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}
