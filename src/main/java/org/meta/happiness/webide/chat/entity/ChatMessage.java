package org.meta.happiness.webide.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.chat.dto.ChatMessageRequestDto;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.userrepo.UserRepo;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //db 에서 생성하고 가지고 있음.
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
