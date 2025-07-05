package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.RoomProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomProgressRepository extends JpaRepository<RoomProgress, Long> {
}
