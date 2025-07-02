package com.exam.tomatoback.web.dto.like.response;

import com.exam.tomatoback.like.model.LikeSort;
import lombok.*;
import org.hibernate.query.SortDirection;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartGetResponse {
    private Integer currentPage;
    private Integer totalPages;
    private Integer size;
    private Long totalElements;
    private LikeSort likeSort;

    private List<CartPost> content;
}
