package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.web.dto.like.request.LikeResponse;
import com.exam.tomatoback.web.dto.like.response.PostLikeResponse;
import com.exam.tomatoback.web.dto.post.image.ImageReponseShort;
import lombok.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseWithImage {
    private Long id;
    private String title;
    private Integer price;
    private PostStatus postStatus;
    private ProductCategory productCategory;
    private LocalDateTime updatedAt;
    // 대표 이미지만 반환하는 필드
    private ImageReponseShort mainImage;

    // Post 엔티티를 PostResponseShort DTO로 변환하는 팩토리 메서드
    public static PostResponseWithImage from(Post post) {
        // Post 엔티티에 연결된 이미지 리스트에서 mainImage가 true인 첫 번째 이미지를 찾습니다.
        ImageReponseShort mainImageSet = null;
        LikeResponse likesSet = null;
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            mainImageSet = post.getImages().stream()
                    .filter(Image::getMainImage)
                    .findFirst()
                    .map(ImageReponseShort::from)
                    .orElse(null);
        }

        return PostResponseWithImage.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .postStatus(post.getPostProgress().getPostStatus())
                .productCategory(post.getProductCategory())
                .updatedAt(post.getUpdatedAt())
                .mainImage(mainImageSet) // 추출한 대표 이미지 DTO를 할당
                .build();
    }
}

