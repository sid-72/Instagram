package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.DTO.PostResponse;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private S3Client s3Client;
    private S3Presigner s3Presigner;


    public String uploadPost(User user, MultipartFile file, String caption) {
        String user_id = user.getId();
        // push to s3
        RequestBody requestBody = null;
        try {
             requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String postId  = UUID.randomUUID().toString();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("sidimagebucket")
                .key(user_id + "/posts/" + postId + "/" + file.getOriginalFilename())
                .build();

        s3Client.putObject(putObjectRequest, requestBody);

        // save image reference in DB
        String image_path = user_id + "/posts/" + postId + "/" + file.getOriginalFilename();

        Post post = Post.builder()
                .id(postId)
                .user(user)
                .image_path(image_path)
                .caption(caption)
                .build();

        postRepository.save(post);
        return post.getId();

    }

    public List<PostResponse> getAllPosts(String userId) {
        List<Post> posts = postRepository.findAll();
        setSingedURLInPosts(posts);
        List<PostResponse> postResponses = null;
        postResponses = posts.stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .user(post.getUser())
                        .image_path(post.getImage_path())
                        .caption(post.getCaption())
                        .comments(post.getComments().stream()
                                .map(comment -> CommentDTO.builder()
                                        .id(comment.getId())
                                        .content(comment.getContent())
                                        .postId(comment.getPost().getId())
                                        .userId(comment.getUser().getId())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()).toList();

        return postResponses;
    }

    public List<PostResponse> getAllPostsOfUser(String userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        System.out.println(posts.toString());
        setSingedURLInPosts(posts);

        List<PostResponse> postResponses = null;
        postResponses = posts.stream()
                .map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .user(post.getUser())
                        .image_path(post.getImage_path())
                        .caption(post.getCaption())
                        .comments(post.getComments().stream()
                                .map(comment -> CommentDTO.builder()
                                        .id(comment.getId())
                                        .content(comment.getContent())
                                        .postId(comment.getPost().getId())
                                        .userId(comment.getUser().getId())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()).toList();

        return postResponses;

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

}
