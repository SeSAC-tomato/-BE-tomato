package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private Long id;
    private boolean mainImage;
    private String url;
    private String savedName;
    private String originalName;
    private LocalDateTime createdAt;

    public static ImageResponse from(Image image) {
        if( image.getPost() == null || image.getPost().getId() == null) {
            throw new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND);
        }

        return ImageResponse.builder()
                .id(image.getId())
                .mainImage(image.isMainImage())
                .url(image.getUrl())
                .savedName(image.getSavedName())
                .originalName(image.getOriginalName())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
