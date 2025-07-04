package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponeWithOwner {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private PostStatus postStatus;
    private ProductCategory productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String nickname;
    private String email;

    public static PostResponeWithOwner from(Post post) {
        return PostResponeWithOwner.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postStatus(post.getPostProgress().getStatus())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .email(post.getUser().getEmail())
                .build();
    }
}