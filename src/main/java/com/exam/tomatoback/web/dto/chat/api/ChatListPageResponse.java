package com.exam.tomatoback.web.dto.chat.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListPageResponse {
    private int currentPage;
    private int size;
    private int totalPages;
    private long totalElements;

    private List<ChatListSingleResponse> rooms;

}
