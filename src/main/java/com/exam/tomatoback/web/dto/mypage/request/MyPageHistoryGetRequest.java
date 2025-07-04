package com.exam.tomatoback.web.dto.mypage.request;

import com.exam.tomatoback.web.dto.like.request.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageHistoryGetRequest {
    @Min(value = 0, message = "currentPage는 0 이상이어야 합니다.")
    private Integer currentSellingPage = 0;

    @Min(value = 0, message = "currentPage는 0 이상이어야 합니다.")
    private Integer currentSoldPage = 0;

    @Min(value = 1, message = "size는 최소 1 이상이어야 합니다.")
    @Max(value = 30, message = "size는 최대 30까지 가능합니다.")
    private Integer sellingSize = 10;

    @Min(value = 1, message = "size는 최소 1 이상이어야 합니다.")
    @Max(value = 30, message = "size는 최대 30까지 가능합니다.")
    private Integer soldSize = 10;

}
