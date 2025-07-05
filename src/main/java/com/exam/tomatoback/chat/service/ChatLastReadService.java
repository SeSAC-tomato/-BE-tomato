package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.Chat;
import com.exam.tomatoback.chat.model.ChatLastRead;
import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.model.RoomUserId;
import com.exam.tomatoback.chat.repository.ChatLastReadRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatLastReadService {
    private final ChatLastReadRepository chatLastReadRepository;


    // 마지막 읽은 채팅 변경
    @Transactional
    public void setLastRead(Chat chatByIdAndRoomId, Room roomById, User requestUser) {

        Optional<ChatLastRead> byIdRoomIdAndIdUserId = chatLastReadRepository.findByIdRoomIdAndIdUserId(roomById.getId(), requestUser.getId());

        ChatLastRead chatLastRead;
        if (byIdRoomIdAndIdUserId.isPresent()) {
            chatLastRead = byIdRoomIdAndIdUserId.get();
            if (chatLastRead.getChat().getId() < chatByIdAndRoomId.getId()) {
                chatLastRead.setChat(chatByIdAndRoomId);
            }
        } else {
            chatLastRead = new ChatLastRead();
            chatLastRead.setId(new RoomUserId(roomById.getId(), requestUser.getId()));
            chatLastRead.setChat(chatByIdAndRoomId);
            chatLastRead.setRoom(roomById);
            chatLastRead.setUser(requestUser);
            chatLastReadRepository.save(chatLastRead);
        }

    }

    // 마지막 읽은 채팅이 있는지
    public boolean existLastReadByChatAndRoomAndUser(Room room, User user) {
        return chatLastReadRepository.existsByIdRoomIdAndIdUserId(room.getId(), user.getId());
    }

    //  마지막 읽은 채팅 가져오기
    public ChatLastRead getLastReadByChatAndRoomAndUser(Room room, User user) {
        return chatLastReadRepository.findByIdRoomIdAndIdUserId(room.getId(), user.getId()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_CHAT_LAST_READ_NOT_FOUND));
    }
}
