package com.exam.tomatoback.web.dto.post.image;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageCreateRequest {
    private Boolean mainImage;
    private String originalName;
    private String url;
}
