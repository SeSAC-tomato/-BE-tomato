package com.exam.tomatoback.web.dto.chat.api;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.web.dto.post.post.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    // about chat
    private long roomId;
    private long senderId;
    private ChatType chatType;

    private long chatId;
    private LocalDateTime createdAt;
    private String content;

    List<String> images;

    // about 이벤트
    private String targetId;
    private PostResponse post;




}
