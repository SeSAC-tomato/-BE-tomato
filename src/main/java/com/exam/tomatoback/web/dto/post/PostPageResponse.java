package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Post;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PostPageResponse {
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private List<PostResponse> posts;

    public static PostPageResponse from( Page<Post> postPage) {
       List<PostResponse> postResponse = postPage
               .stream()
               .map(PostResponse::from)
               .collect(Collectors.toList());

       return PostPageResponse.builder()
               .page(postPage.getNumber())
               .size(postPage.getSize())
               .totalCount(postPage.getTotalElements())
               .totalPages(postPage.getTotalPages())
               .posts(postResponse)
               .build();
    }
}
