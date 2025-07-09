package com.exam.tomatoback.post.model;

import com.exam.tomatoback.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="post_progress")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false, unique = true, updatable=false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=true, updatable=true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PostStatus postStatus = PostStatus.SELLING;
}
