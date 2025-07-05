package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findFirstByRoomIdOrderByCreatedAtDesc(long id);

    Page<Chat> findByRoomIdOrderByIdDesc(long roomId, Pageable pageable);

    Optional<Chat> findByIdAndRoomId(Long id, long roomId);

    long countByRoomIdAndIdGreaterThan(long roomId, Long id);
}
