package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.model.Comment;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.CommentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.Path2;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private UnifiedJedis redis;
    private ObjectMapper objectMapper;


    public CommentDTO addComment(CommentDTO commentDTO, Post post, User user) {
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .post(post)
                .content(commentDTO.getContent())
                .user(user)
                .build();

        commentRepository.save(comment);
        try {
            redis.jsonSet("post:" + post.getId(), new Path2("$.comments"),
                    objectMapper.writeValueAsString(comment.toDTO()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("updated comment list in redis: " +
                redis.jsonGet("post:" + post.getId(), new Path2("$.comments")));
        return comment.toDTO();
    }

    public Optional<CommentDTO> getComment(String commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> comment.toDTO());

    }
}
