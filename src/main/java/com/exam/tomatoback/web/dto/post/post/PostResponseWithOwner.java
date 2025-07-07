package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import com.exam.tomatoback.web.dto.post.image.ImageReponseShort;
import com.exam.tomatoback.web.dto.post.image.ImageResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.log.SubSystemLogging;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class PostResponseWithOwner {
    private Long id;
    private String title;
    private Integer price;
    private String content;
    private PostStatus postStatus;
    private ProductCategory productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String nickname;
    private List<ImageReponseShort> images;

    public static PostResponseWithOwner from(Post post) {
        List<ImageReponseShort> imageResponses = null;
        if (post.getImages() != null) {
            imageResponses = post.getImages().stream()
                    .map(ImageReponseShort::from)
                    .collect(Collectors.toList());
        }
        return PostResponseWithOwner.builder()
                .id(post.getId())
                .title(post.getTitle())
                .price(post.getPrice())
                .content(post.getContent())
                .postStatus(post.getPostProgress().getPostStatus())
                .productCategory(post.getProductCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .images(imageResponses)
                .build();
    }
}