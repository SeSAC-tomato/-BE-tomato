package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.web.dto.post.image.ImageUpdateRequest;
import lombok.*;

import java.util.List;

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
    private ProductCategory productCategory;
    private List<ImageUpdateRequest> imageInfo;


    public Post toDomain() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .content(this.content)
                .productCategory(this.productCategory)
                .build();
    }
}
