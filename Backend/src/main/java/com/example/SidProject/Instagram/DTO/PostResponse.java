package com.example.SidProject.Instagram.DTO;

import com.example.SidProject.Instagram.model.Comment;
import com.example.SidProject.Instagram.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostResponse {
    private String postId;
    private String caption;
    private List<CommentDTO> comments;
    private User user;
    private String image_path;

}
