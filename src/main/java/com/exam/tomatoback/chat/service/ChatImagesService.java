package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.ChatImages;
import com.exam.tomatoback.chat.repository.ChatImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatImagesService {
    private final ChatImagesRepository chatImagesRepository;

    public ChatImages save(ChatImages chatImages) {
        return chatImagesRepository.save(chatImages);
    }

    public List<String> getChatImagesByChatId(Long id) {
        List<ChatImages> byChatId = chatImagesRepository.findByChatId(id);
        List<String> list = byChatId.stream().map(chatImage -> chatImage.getSavedName()).toList();

        return list;
    }
}
