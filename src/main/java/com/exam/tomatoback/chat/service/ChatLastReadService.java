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



    @Transactional
    public void setLastRead(Chat chatByIdAndRoomId, Room roomById, User requestUser) {

        System.out.println(chatByIdAndRoomId.getId());
        System.out.println(roomById.getId());
        System.out.println(requestUser.getId());

        Optional<ChatLastRead> byIdRoomIdAndIdUserId = chatLastReadRepository.findByIdRoomIdAndIdUserId(roomById.getId(), requestUser.getId());


        ChatLastRead chatLastRead;
        if (byIdRoomIdAndIdUserId.isPresent()) {
            chatLastRead = byIdRoomIdAndIdUserId.get();
            if (chatLastRead.getChat().getId() < chatByIdAndRoomId.getId()) {
                chatLastRead.setChat(chatByIdAndRoomId);
//                chatLastReadRepository.save(chatLastRead);
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

    public boolean existLastReadByChatAndRoomAndUser(Chat lastChat, Room room, User user) {
//        return chatLastReadRepository.existsByChatIdAndIdRoomIdAndIdUserId(lastChat.getId(), room.getId(), user.getId());
        return  chatLastReadRepository.existsByIdRoomIdAndIdUserId(room.getId(), user.getId());

//        return chatLastReadRepository.existsByIdAndChatId(new RoomUserId(room.getId(), user.getId()), lastChat.getId());
    }

    public ChatLastRead getLastReadByChatAndRoomAndUser(Chat lastChat, Room room, User user) {

        return chatLastReadRepository.findByIdRoomIdAndIdUserId( room.getId(), user.getId()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_CHAT_LAST_READ_NOT_FOUND));
//        return chatLastReadRepository.findByIdAndChatId(new RoomUserId(room.getId(), user.getId()), lastChat.getId()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_CHAT_LAST_READ_NOT_FOUND));
    }
}
