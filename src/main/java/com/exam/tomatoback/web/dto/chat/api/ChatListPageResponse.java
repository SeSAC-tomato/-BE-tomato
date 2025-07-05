package com.exam.tomatoback.web.dto.chat.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatListPageResponse extends DefaultPageResponse{
    private List<ChatListSingleResponse> rooms;
}
