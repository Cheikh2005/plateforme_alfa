package com.alfaconseil.bacprep.repository;

import com.alfaconseil.bacprep.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserIdOrderByDateMessageAsc(Long userId);
    List<ChatMessage> findByUserIdOrderByDateMessageDesc(Long userId, Pageable pageable);
}