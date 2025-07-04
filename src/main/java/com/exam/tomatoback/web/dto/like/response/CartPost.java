package com.exam.tomatoback.web.dto.like.response;

import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.post.model.PostStatus;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartPost {

        private String title;
        private Integer price;
        private String img;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private PostStatus postStatus;
        private ProductCategory productCategory;
}
