package com.exam.tomatoback.web.dto.post.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetadataRequest {
      private String originalFileName;
      private Boolean isMain;
}
