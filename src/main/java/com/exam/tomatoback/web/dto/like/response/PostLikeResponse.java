package com.exam.tomatoback.web.dto.like.response;

import com.exam.tomatoback.like.model.Like;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.web.dto.post.image.ImageReponseShort;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostLikeResponse {
    Long likeId;
    Long postId;
    Long userId;

    public static PostLikeResponse from(Like like) {
        return PostLikeResponse.builder()
                .likeId(like.getId())
                .postId(like.getPost().getId())
                .userId(like.getUser().getId())
                .build();
    }
}
