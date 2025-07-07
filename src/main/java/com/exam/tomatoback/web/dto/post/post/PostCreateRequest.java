package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostProgress;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.web.dto.post.image.ImageCreateRequest;
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
        private ProductCategory productCategory;
        private Boolean deleted;
        private List<ImageCreateRequest>  imageInfo;

        public Post toDomain() {
            Post newPost = Post.builder()
                    .title(this.title)
                    .price(this.price)
                    .content(this.content)
                    .productCategory(this.productCategory)
                    .deleted(false)
                    .build();
            return newPost;
        }
}
