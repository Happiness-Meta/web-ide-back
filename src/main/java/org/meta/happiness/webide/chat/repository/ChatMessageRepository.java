package org.meta.happiness.webide.chat.repository;

import org.meta.happiness.webide.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    Optional<List<ChatMessage>> findByRepoId(Long repoId);
}
