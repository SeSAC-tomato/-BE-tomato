package com.exam.tomatoback.web.dto.chat.api;


import com.exam.tomatoback.chat.enums.RoomProgressEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomInfoResponse {
    private long roomId;
    private ChatPostResponse targetPost;

    private long requestUserId;
    private RoomProgressEnum roomProgress;
}
