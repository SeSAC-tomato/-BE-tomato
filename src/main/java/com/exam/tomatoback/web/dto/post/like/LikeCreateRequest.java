package com.exam.tomatoback.web.dto.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeCreateRequest {
    Long postId;
}
