package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.PostCategory;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
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
    private PostStatus postStatus;
    private PostCategory postCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    public static PostResponse from(Post post) {
        if( post.getUser() == null || post.getUser().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_USER_NOT_FOUND);
        }

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postStatus(post.getPostStatus())
                .postCategory(post.getPostCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .build();
    }
}
