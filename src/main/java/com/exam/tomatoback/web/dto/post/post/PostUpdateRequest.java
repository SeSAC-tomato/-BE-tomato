package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostProgress;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
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
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .content(this.content)
                .postProgress(PostProgress.builder()
                        .postStatus(this.postStatus)
                        .build())
                .productCategory(this.productCategory)
                .build();
    }
}
