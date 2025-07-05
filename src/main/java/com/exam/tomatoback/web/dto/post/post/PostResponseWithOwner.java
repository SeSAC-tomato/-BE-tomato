package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.log.SubSystemLogging;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class PostResponseWithOwner {
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

    public static PostResponseWithOwner from(Post post) {
        log.info(post.getUser().getNickname());
        log.info(post.getUser().toString());

        return PostResponseWithOwner.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postStatus(post.getPostProgress().getPostStatus())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .build();
    }
}