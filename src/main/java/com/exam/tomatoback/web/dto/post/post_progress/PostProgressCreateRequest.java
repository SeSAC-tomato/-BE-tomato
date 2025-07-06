package com.exam.tomatoback.web.dto.post.post_progress;

import com.exam.tomatoback.post.model.PostStatus;
import lombok.*;

@Getter
@Setter
@Builder
public class PostProgressCreateRequest {
    String postId;
}
