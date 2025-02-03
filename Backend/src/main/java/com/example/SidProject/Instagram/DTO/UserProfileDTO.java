package com.example.SidProject.Instagram.DTO;

import com.example.SidProject.Instagram.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private User user;
    private List<PostResponse> postResponses;
}
