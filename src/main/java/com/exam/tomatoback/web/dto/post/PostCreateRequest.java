package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.user.model.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
        private String title;
        private Integer price;
        private String content;
        private PostStatus status;
        private ProductCategory productCategory;
        private Boolean deleted;
        private Long userId;
        private List<ImageResponse> images;
}
