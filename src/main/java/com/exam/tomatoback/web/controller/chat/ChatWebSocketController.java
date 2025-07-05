package com.exam.tomatoback.web.controller.chat;

import com.exam.tomatoback.chat.service.ChatWebSocketService;
import com.exam.tomatoback.web.dto.chat.api.ChatResponse;
import com.exam.tomatoback.web.dto.chat.websocket.ChatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatWebSocketService chatWebSocketService;
    private final SimpMessageSendingOperations sendingOperations;


    // 메시지 보내는 엔드포인트

    @MessageMapping("/room/{roomId}")
    public void sendMessage(@DestinationVariable long roomId, Principal principal, @Valid ChatRequest chatRequest) throws IOException {

        ChatResponse chatResponse = chatWebSocketService.saveChat(roomId, principal, chatRequest);

        sendingOperations.convertAndSend("/ws/sub/room/" + roomId, chatResponse);

    }

}
