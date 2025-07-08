package com.exam.tomatoback.web.dto.post.post;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionResponse {
    private List<String> regions;
}
