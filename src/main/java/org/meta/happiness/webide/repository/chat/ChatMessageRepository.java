package org.meta.happiness.webide.repository.chat;

import org.meta.happiness.webide.entity.chat.ChatMessage;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<List<ChatMessage>> findAllByUserRepoIn(List<UserRepo> userRepo);
}
