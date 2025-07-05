package com.exam.tomatoback.web.dto.chat.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPageResponse {
    private long roomId;
    private int size;
    private int currentPage;
    private long totalElements;
    private int totalPages;

    private List<ChatResponse> content;
}