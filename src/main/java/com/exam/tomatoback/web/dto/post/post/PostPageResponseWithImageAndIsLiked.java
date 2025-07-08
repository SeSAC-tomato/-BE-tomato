package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageResponseWithImageAndIsLiked {
    private List<PostResponseWithImageAndIsLiked> posts;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static PostPageResponseWithImageAndIsLiked from(Page<Post> postPage, List<PostResponseWithImageAndIsLiked> postDTOs) {
        return PostPageResponseWithImageAndIsLiked.builder()
                .posts(postDTOs)
                .currentPage(postPage.getNumber())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();
    }
}
