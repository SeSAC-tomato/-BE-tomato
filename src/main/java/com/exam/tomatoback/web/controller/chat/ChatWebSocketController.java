package com.exam.tomatoback.web.controller.chat;

import com.exam.tomatoback.chat.service.ChatWebSocketService;
import com.exam.tomatoback.infrastructure.util.ChatImageSaver;
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
    private final ChatImageSaver  chatImageSaver;


    @MessageMapping("/room/{roomId}")
    public void sendMessage(@DestinationVariable long roomId, Principal principal, @Valid ChatRequest chatRequest) throws IOException {


//        // 클라이언트로부터 STOMP 메시지 body로 받은 Base64 인코딩된 문자열이라고 가정
//        String receivedBase64String = chatRequest.getImages().get(0); // 예시 문자열
//
//        // 1. Base64 문자열 자체의 길이 (문자 수)
//        int base64StringLength = receivedBase64String.length();
//        System.out.println("받은 Base64 문자열의 길이 (디코딩 전): " + base64StringLength + " 문자");
//
//        // 2. Base64 문자열의 바이트 크기 (디코딩 전)
//        // String.getBytes()는 기본 인코딩(UTF-8 등)에 따라 바이트 배열을 반환합니다.
//        // Base64는 ASCII 문자만 사용하므로 보통 1문자당 1바이트로 나옵니다.
//        byte[] rawBytes = receivedBase64String.getBytes(StandardCharsets.UTF_8); // 또는 StandardCharsets.UTF_8
//        long rawByteSize = rawBytes.length;
//
//        System.out.println("받은 Base64 문자열의 바이트 크기 (디코딩 전): " + rawByteSize + " 바이트");
//        System.out.println("받은 Base64 문자열의 MB 크기 (디코딩 전): " + String.format("%.2f", (double) rawByteSize / (1024 * 1024)) + " MB");
//

        //
        ChatResponse chatResponse = chatWebSocketService.saveChat(roomId, principal, chatRequest);

        sendingOperations.convertAndSend("/ws/sub/room/" + roomId, chatResponse);

    }

    @MessageMapping("/error-trigger")
    public void triggerError() {

        throw new RuntimeException("서버에서 발생한 의도적인 오류입니다!");
    }


}
