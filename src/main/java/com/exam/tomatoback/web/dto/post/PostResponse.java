package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Categories;
import com.exam.tomatoback.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private long id;
    private String title;
    private int price;
    private String content;
    private String status;
    private Categories category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public static PostResponse from(Post post) {
        if( post.getUser() == null || post.getUser().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_USER_NOT_FOUND);
        }

        return PostResponse.builder()
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .status(post.getStatus())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .build();
    }
}
