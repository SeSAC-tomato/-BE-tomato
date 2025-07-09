package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private PostStatus postStatus;
    private ProductCategory productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private List<Image> images;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postStatus(post.getPostProgress().getPostStatus())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .build();
    }
}
