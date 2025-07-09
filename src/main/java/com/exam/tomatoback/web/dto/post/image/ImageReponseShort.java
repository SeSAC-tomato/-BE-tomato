package com.exam.tomatoback.web.dto.post.image;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Image;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageReponseShort {
    private Long id;
    private Boolean mainImage;
    private String url;
    private String savedName;
    private String originalName;

    public static ImageReponseShort from(Image image) {
        return ImageReponseShort.builder()
                .id(image.getId())
                .mainImage(image.getMainImage())
                .url(image.getUrl())
                .savedName(image.getSavedName())
                .build();
    }
}
