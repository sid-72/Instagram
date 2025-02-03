package com.example.SidProject.Instagram.model;

import com.example.SidProject.Instagram.DTO.CommentDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Table(name="Comments")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties({"post", "user"})
public class Comment {
    @Id
    private String id;
    @NonNull
    private String content;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NonNull
    private User user;

    public CommentDTO toDTO(Post post, User user) {
    
        return CommentDTO.builder()
                .id(this.id)
                .content(this.content)
                .postId(this.post.getId())
                .userId(this.user.getId())
                .build();
    
    }

    
}
