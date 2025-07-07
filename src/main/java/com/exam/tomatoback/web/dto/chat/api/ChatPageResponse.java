package com.exam.tomatoback.web.dto.chat.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatPageResponse  extends DefaultPageResponse{
    private long roomId;
    private List<ChatResponse> content;
}