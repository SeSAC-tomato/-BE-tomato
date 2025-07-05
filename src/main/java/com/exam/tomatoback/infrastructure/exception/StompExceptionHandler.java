package com.exam.tomatoback.infrastructure.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.security.Principal;

@ControllerAdvice
public class StompExceptionHandler {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public StompExceptionHandler(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(Exception exception, Principal principal) {
        String email = principal.getName();

        simpMessagingTemplate.convertAndSend("/ws/sub/user/"+email+"/queue/errors", "서버 오류: " + exception.getMessage());

    }
}
