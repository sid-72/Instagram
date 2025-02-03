package com.example.SidProject.Instagram.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;
    private String userId;
    private MultipartFile profileImage;
}
