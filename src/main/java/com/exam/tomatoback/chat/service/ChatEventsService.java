package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.ChatEvents;
import com.exam.tomatoback.chat.repository.ChatEventsRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatEventsService {
    private final ChatEventsRepository chatEventsRepository;

    public ChatEvents save(ChatEvents chatEvents) {
        return chatEventsRepository.save(chatEvents);
    }

    public ChatEvents getChatEventByChatId(Long id) {
        Optional<ChatEvents> chatEventsOptional = chatEventsRepository.findByChatId((id));
        return  chatEventsOptional.orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_CHAT_EVENT_CAN_NOT_FOUND));
    }
}
