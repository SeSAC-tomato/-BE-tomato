package com.exam.tomatoback.web.dto.chat.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {
    private long roomId;

    private long targetUserId;

    private String targetUserNickname;

}
