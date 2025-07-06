package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.*;
import com.exam.tomatoback.web.dto.post.image.ImageResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

//Post목록을 조회하고 이미지도 함께 가져옴
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponse {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private PostProgress postProgress;
    private ProductCategory productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String mainImageUrl;
    private List<ImageResponse> images;

    public static PostDetailResponse from(Post post, List<Image> images) {
        List<ImageResponse> imagesResponses = images.stream()
                .map(ImageResponse::from)
                .toList();

        String mainImageUrl = imagesResponses.stream()
                .filter(ImageResponse::getMainImage)
                .findFirst()
                .map(ImageResponse::getUrl)
                .orElse(null);

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postProgress(post.getPostProgress())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .mainImageUrl(mainImageUrl)
                .images(imagesResponses)
                .build();
    }
}
