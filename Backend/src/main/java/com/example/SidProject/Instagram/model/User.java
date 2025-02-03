package com.example.SidProject.Instagram.model;

import com.example.SidProject.Instagram.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties({"posts", "comments"})
@Table(name = "Users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String profileImagePath;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

}
