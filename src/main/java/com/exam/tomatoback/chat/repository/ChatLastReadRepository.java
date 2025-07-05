package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.ChatLastRead;
import com.exam.tomatoback.chat.model.RoomUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatLastReadRepository extends JpaRepository<ChatLastRead, RoomUserId> {
    Optional<ChatLastRead> findByIdRoomIdAndIdUserId(long roomId, long userId);

    boolean existsByIdRoomIdAndIdUserId(long roomId, long userId);

}
