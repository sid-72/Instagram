package com.example.SidProject.Instagram.DTO;

import com.example.SidProject.Instagram.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {
    private String id;
    private String content;
    private String postId;
    private String userId;
}
