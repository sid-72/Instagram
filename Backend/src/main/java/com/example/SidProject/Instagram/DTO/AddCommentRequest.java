package com.example.SidProject.Instagram.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AddCommentRequest {
    private String content;
    private String userId;
    private String postId;
}
