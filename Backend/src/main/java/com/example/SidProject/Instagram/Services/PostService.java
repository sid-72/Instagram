package com.example.SidProject.Instagram.Services;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.example.SidProject.Instagram.DTO.PostResponse;
import com.example.SidProject.Instagram.model.Post;
import com.example.SidProject.Instagram.model.User;
import com.example.SidProject.Instagram.repositories.PostRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private MinioClient minioClient;


    public String uploadPost(User user, MultipartFile file, String caption) {
        String user_id = user.getId();
        String postId  = UUID.randomUUID().toString();
        String objectName = user_id + "/posts/" + postId + "/" + file.getOriginalFilename();

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

        // save image reference in DB
        String image_path = objectName;

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

}
