package com.exam.tomatoback.web.dto.post.like;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeCreateRequest {
    Long postId;
}
