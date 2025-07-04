package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.like.model.Like;
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
    private LocalDateTime createdAt;

    public static LikeResponse from(Like like) {
        if( like.getPost() == null || like.getPost().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND);
        }

        return LikeResponse.builder()
                .id(like.getId())
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .createdAt(like.getCreatedAt())
                .build();
    }
}
