package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
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
    private PostStatus postStatus;
    private ProductCategory productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String mainImageUrl;
    private List<ImageResponse> images;

    public static PostDetailResponse from(Post post, List<Image> images) {
        if( post.getUser() == null || post.getUser().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_USER_NOT_FOUND);
        }

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
                .postStatus(post.getStatus())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .mainImageUrl(mainImageUrl)
                .images(imagesResponses)
                .build();
    }
}
