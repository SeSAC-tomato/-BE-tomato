package com.exam.tomatoback.post.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @Column(nullable = false)
    private String url;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
