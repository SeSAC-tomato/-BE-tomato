package com.exam.tomatoback.web.dto.chat.api;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.web.dto.post.PostResponse;
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
    private long chatId;
    private long roomId;
    private long senderId;
    private String content;
    private LocalDateTime createdAt;
    private ChatType chatType;

    // about 이벤트
    private String targetId;
    private PostResponse post;
    private Boolean isEventDone;

    // about images
    List<String> images;



}
