package com.exam.tomatoback.web.dto.post.image;

import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageCreateRequest {
    private String savedName;
    private String originalName;
    private Boolean mainImage;

    public Image toDomain() {
        return Image.builder()
                .savedName(this.savedName)
                .originalName(this.originalName)
                .mainImage(this.mainImage)
                .build();
    }
}
