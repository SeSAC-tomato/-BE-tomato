package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.ChatImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatImagesRepository extends JpaRepository<ChatImages, Long> {
    List<ChatImages> findByChatId(long chatId);
}
