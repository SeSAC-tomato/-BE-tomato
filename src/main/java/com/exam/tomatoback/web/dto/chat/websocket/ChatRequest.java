package com.exam.tomatoback.web.dto.chat.websocket;


import com.exam.tomatoback.chat.annotation.ValidChatRequest;
import com.exam.tomatoback.chat.enums.ChatType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidChatRequest
public class ChatRequest {

    @Positive
    private Long roomId;

    @NotNull
    private ChatType chatType;

    private List<String> images;

    private String content;

    // 이벤트시 postId
    private Long targetId;


}
