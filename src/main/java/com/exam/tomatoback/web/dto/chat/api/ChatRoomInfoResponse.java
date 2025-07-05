package com.exam.tomatoback.web.dto.chat.api;


import com.exam.tomatoback.chat.enums.RoomProgressEnum;
import com.exam.tomatoback.web.dto.post.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomInfoResponse {
    private long roomId;
    private PostResponse targetPost;

    private long requestUserId;
    private RoomProgressEnum roomProgress;
}
