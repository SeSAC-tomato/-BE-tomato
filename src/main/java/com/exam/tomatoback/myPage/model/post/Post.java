package com.exam.tomatoback.mypage.model.post;

import com.exam.tomatoback.mypage.model.enums.Category;
import com.exam.tomatoback.mypage.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    private String title;
    private String content;
    private int price;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int deleted;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne(mappedBy = "post")
    private PostProgress progress;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToOne(mappedBy = "post")
    private Image image;
}
