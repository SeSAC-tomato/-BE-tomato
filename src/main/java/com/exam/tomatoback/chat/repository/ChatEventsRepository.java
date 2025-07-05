package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.ChatEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatEventsRepository extends JpaRepository<ChatEvents, Long> {
}
