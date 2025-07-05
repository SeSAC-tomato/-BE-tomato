package com.exam.tomatoback.web.dto.chat.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultPageResponse {
    private int currentPage;
    private int size;
    private int totalPages;
    private long totalElements;
}
