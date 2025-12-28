package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.DTO.PostResponse;
import com.example.SidProject.Instagram.DTO.UserDTO;
import com.example.SidProject.Instagram.DTO.UserProfileDTO;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
import com.example.SidProject.Instagram.repositories.UserRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MinioClient minioClient;
    private final PostRepository postRepository;




    public UserProfileDTO getUserProfile(String userId) throws RuntimeException {
        System.out.println("userId is:" + userId);
        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        List<Post> posts = postRepository.findByUserId(userId);
        setSingedURLInPosts(posts);
        return UserProfileDTO.builder()
                .user(user)
                .postResponses(posts.stream().map(post -> PostResponse.builder()
                                .caption(post.getCaption())
                                .postId(post.getId())
                                .comments(post.getComments().stream()
                                        .map(comment -> CommentDTO.builder()
                                                .id(comment.getId())
                                                .content(comment.getContent())
                                                .postId(comment.getPost().getId())
                                                .userId(comment.getUser().getId())
                                                .build())
                                        .collect(Collectors.toList()))
                                .image_path(post.getImage_path())
                                .build())
                        .toList())
                .build();
    }

    public void addUser(UserDTO userDTO) {
        String userId =  userDTO.getUserId();
        MultipartFile file =  userDTO.getProfileImage();

        String objectName = userId + "/profilePicture/" + file.getOriginalFilename();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("sidimagebucket")
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String imagePath = objectName;

        User user = User.builder()
                .id(userDTO.getUserId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .profileImagePath(imagePath)
                .build();

        userRepository.save(user);
    }

    /**
     * Checks if a username already exists in the database
     * @param username The username to check (stored in the name field)
     * @return boolean - true if username exists, false otherwise
     */
    public boolean isUsernameExists(String userId) {
        return userRepository.findById(userId).isPresent();
    }

    private void setSingedURLInPosts(List<Post> posts) {
        for(Post post: posts) {
            try {
                String preSignedURL = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket("sidimagebucket")
                                .object(post.getImage_path())
                                .expiry(10 * 60)
                                .build()
                );

                post.setImage_path(preSignedURL);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }
}
