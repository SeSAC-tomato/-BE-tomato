package com.exam.tomatoback.web.dto.like.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeCreateRequest {
    Long postId;
}

