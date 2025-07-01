package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;

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

        public Post toDomain() {
            Post newPost = Post.builder()
                    .title(this.title)
                    .price(this.price)
                    .content(this.content)
                    .status(this.status)
                    .productCategory(this.productCategory)
                    .deleted(false)
                    .build();
            return newPost;
        }
}
