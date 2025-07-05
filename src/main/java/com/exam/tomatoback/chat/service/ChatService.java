package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.Chat;
import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.repository.ChatRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Chat save(Chat newChat) {
        return chatRepository.save(newChat);
    }

    public Optional<Chat> getLastChatByRoomId(Long id) {
        return chatRepository.findFirstByRoomIdOrderByCreatedAtDesc(id);
    }

    public Page<Chat> getChats(long roomId, Pageable pageable) {
        return chatRepository.findByRoomIdOrderByIdDesc(roomId, pageable);
    }

    public Chat getChatByIdAndRoomId(long chatId, long roomId) {
        return chatRepository.findByIdAndRoomId(chatId, roomId).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_CHAT_NOT_FOUND));
    }

    public long countAfterChatId(Chat lastChat, Room room) {
        return chatRepository.countByRoomIdAndIdGreaterThan(room.getId(), lastChat.getId());
    }

    public long countAfterChatId(long lastChatId, Room room) {
        return chatRepository.countByRoomIdAndIdGreaterThan(room.getId(), lastChatId);
    }
}
