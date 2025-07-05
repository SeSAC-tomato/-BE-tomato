package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.ChatLastRead;
import com.exam.tomatoback.chat.model.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatLastReadRepository extends JpaRepository<ChatLastRead, RoomUserId> {

    boolean existsByChatIdAndIdRoomIdAndIdUserId(long chatId, long roomId, long userId);

    Optional<ChatLastRead> findByChatIdAndIdRoomIdAndIdUserId(long chatId, long roomId, long userId);

    Optional<ChatLastRead> findByIdRoomIdAndIdUserId(long roomId, long userId);
    boolean existsByIdRoomIdAndIdUserId(long roomId, long userId);

//    boolean existsByChatIdAndRoomIdAndUserId(long chatId, long roomId, long userId);
//    Optional<ChatLastRead> findByChatIdAndRoomIdAndUserId(long chatId, long roomId, long userId);
//    Optional<ChatLastRead> findByIdAndChatId(RoomUserId roomUserId, long chatId);
//    boolean existsByIdAndChatId(RoomUserId roomUserId, long chatId);
}
