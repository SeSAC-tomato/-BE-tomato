package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.*;
import com.exam.tomatoback.web.dto.post.image.ImageReponseShort;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateResponse {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private ProductCategory productCategory;
    private Long userId;
    private String mainImageUrl;
    private List<ImageReponseShort> images;


    public static PostUpdateResponse from(Post post, List<Image> images) {
        List<ImageReponseShort> imagesResponses = images.stream()
                .map(ImageReponseShort::from)
                .toList();

        String mainImageUrl = imagesResponses.stream()
                .filter(ImageReponseShort::getMainImage)
                .findFirst()
                .map(ImageReponseShort::getUrl)
                .orElse(null);

        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .productCategory(post.getProductCategory())
                .userId(post.getUser().getId())
                .mainImageUrl(mainImageUrl)
                .images(imagesResponses)
                .build();
    }
}
