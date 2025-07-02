package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Like;
import com.exam.tomatoback.post.model.Post;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeCreateRequest {
    Long postId;
    Long userId;
}
