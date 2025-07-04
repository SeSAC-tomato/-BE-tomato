package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.post.model.PostStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private PostStatus postStatus;
    private ProductCategory productCategory;


    public Post toDomain() {
        Post newPost = Post.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .content(this.content)
                .postStatus(this.postStatus)
                .productCategory(this.productCategory)
                .build();
        return newPost;
    }
}
