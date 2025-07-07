package com.exam.tomatoback.web.dto.chat.api;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {
    @Positive
    private long targetUserId;
}
