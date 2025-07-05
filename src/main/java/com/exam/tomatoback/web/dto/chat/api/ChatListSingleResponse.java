package com.exam.tomatoback.web.dto.chat.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatListSingleResponse {
    private String userNickname;
    private LocalDateTime lastChatTime;
    private ChatResponse lastChat;
    private long roomId;
    private int unreadCount;
}
