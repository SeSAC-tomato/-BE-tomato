package com.exam.tomatoback.web.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageCreateRequest {
    private boolean mainImage;
    private String originalName;
    private String url;
}
