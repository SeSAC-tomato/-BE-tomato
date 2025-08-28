package com.exam.tomatoback.web.dto.post.image;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageUpdateRequest {
    private Long id;
    private Boolean mainImage;
    private String savedName;
    private String originalName;
}
