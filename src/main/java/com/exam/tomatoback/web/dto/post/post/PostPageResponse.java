package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class PostPageResponse {
    private Integer page;
    private Integer size;
    private Long totalCount;
    private Integer totalPages;
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
