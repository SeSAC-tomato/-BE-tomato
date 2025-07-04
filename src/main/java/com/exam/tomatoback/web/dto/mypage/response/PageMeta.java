package com.exam.tomatoback.web.dto.mypage.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageMeta {
    private int currentPage;
    private int totalPages;
    private int size;
    private long totalElements;
}

