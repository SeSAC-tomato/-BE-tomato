package com.exam.tomatoback.web.dto.post.like;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Like;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {
    Long id;
    Long postId;
    Long userId;
    Boolean isLiked;
    private LocalDateTime createdAt;

    public static LikeResponse from(Like like, Boolean isLiked) {
        if( like.getPost() == null || like.getPost().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND);
        }

        return LikeResponse.builder()
                .id(like.getId())
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .isLiked(isLiked)
                .createdAt(like.getCreatedAt())
                .build();
    }
}
