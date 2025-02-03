package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.DTO.PostResponse;
import com.example.SidProject.Instagram.DTO.UserDTO;
import com.example.SidProject.Instagram.DTO.UserProfileDTO;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
import com.example.SidProject.Instagram.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Client s3Client;
    private final PostRepository postRepository;
    private S3Presigner s3Presigner;




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

        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("sidimagebucket")
                .key(userId + "/profilePicture/" + file.getOriginalFilename())
                .build();

        s3Client.putObject(putObjectRequest, requestBody);
        String imagePath = userId + "/profilePicture/" + file.getOriginalFilename();

        User user = User.builder()
                .id(userDTO.getUserId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .profileImagePath(imagePath)
                .build();

        userRepository.save(user);
    }

    private void setSingedURLInPosts(List<Post> posts) {
        for(Post post: posts) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket("sidimagebucket")
                    .key(post.getImage_path())
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest preSignedRequest = s3Presigner.presignGetObject(presignRequest);

            String preSignedURL = preSignedRequest.url().toString();

            post.setImage_path(preSignedURL);

        }
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId);
    }
}
