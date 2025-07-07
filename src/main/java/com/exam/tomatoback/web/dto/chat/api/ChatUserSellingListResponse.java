package com.exam.tomatoback.web.dto.chat.api;

import com.exam.tomatoback.web.dto.post.post.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUserSellingListResponse {
    private long targetUserId;
    private List<ChatPostResponse> posts;
}
