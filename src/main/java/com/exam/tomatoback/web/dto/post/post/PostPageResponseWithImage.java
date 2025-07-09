package com.exam.tomatoback.web.dto.post.post;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.web.dto.post.image.ImageReponseShort;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageResponseWithImage {
    private List<PostResponseWithImage> posts; // 각 게시글 정보를 담는 리스트
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private ImageReponseShort mainImage;
    public static PostPageResponseWithImage from(Page<Post> post) {
        // Post 엔티티에 연결된 이미지 리스트에서 mainImage가 true인 첫 번째 이미지를 찾습니다.
        List<PostResponseWithImage> postResponseWithImage = post.getContent().stream()
                .map(PostResponseWithImage::from) // PostResponseShort.from() 메서드를 호출하여 변환
                .collect(Collectors.toList());

        return PostPageResponseWithImage.builder()
                .posts(postResponseWithImage)
                .currentPage(post.getNumber())
                .totalPages(post.getTotalPages())
                .totalElements(post.getTotalElements())
                .build();

    }

}
