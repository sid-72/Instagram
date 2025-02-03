package com.example.SidProject.Instagram.repositories;

import com.example.SidProject.Instagram.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findByUserId(String userId);
}
