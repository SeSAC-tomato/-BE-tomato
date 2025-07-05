package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.ChatEvents;
import com.exam.tomatoback.chat.repository.ChatEventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatEventsService {
    private final ChatEventsRepository chatEventsRepository;

    public ChatEvents save(ChatEvents chatEvents) {
        ChatEvents saved = chatEventsRepository.save(chatEvents);
        return  saved;
    }
}
