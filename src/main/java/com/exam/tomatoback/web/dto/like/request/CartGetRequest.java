package com.exam.tomatoback.web.dto.like.request;

import com.exam.tomatoback.web.dto.like.request.LikeSort;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartGetRequest {
    @Min(value = 0, message = "currentPage는 0 이상이어야 합니다.")
    private Integer currentPage = 0;

    @Min(value = 1, message = "size는 최소 1 이상이어야 합니다.")
    @Max(value = 30, message = "size는 최대 30까지 가능합니다.")
    private Integer size = 10;
    @NotNull(message = "정렬 기준(likeSort)을 입력해주세요.")
    private LikeSort likeSort;

}
