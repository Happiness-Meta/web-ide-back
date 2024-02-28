package org.meta.happiness.webide.entity.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.chat.ChatMessageRequestDto;
import org.meta.happiness.webide.dto.chat.ChatMessageType;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.userrepo.UserRepo;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userrepo_id")
    private UserRepo userRepo;

    @Column(length = 255, nullable = false)
    private String messageContent;

    private ChatMessageType chatMessageType;

    public static ChatMessage createChatMessage(UserRepo userRepo, ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.chatMessageType = chatMessageRequestDto.getType();
        chatMessage.messageContent = chatMessageRequestDto.getContent();
        chatMessage.userRepo = userRepo;
        return chatMessage;
    }
}
