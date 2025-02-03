package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.model.Comment;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;


    public CommentDTO addComment(CommentDTO commentDTO, Post post, User user) {
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .post(post)
                .content(commentDTO.getContent())
                .user(user)
                .build();

        commentRepository.save(comment);
        return comment.toDTO(post, user);
    }

    public Optional<CommentDTO> getComment(String commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.toDTO(comment.getPost(), comment.getUser()));

    }
}
