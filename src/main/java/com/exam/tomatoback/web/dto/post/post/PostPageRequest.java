package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageRequest {
    private Integer page;
    private Integer size;
    private String keyword;
    private ProductCategory productCategory;
//    private String region;
    private Boolean selling;
    private Integer minPrice;
    private Integer maxPrice;
}
