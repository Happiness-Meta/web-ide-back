package org.meta.happiness.webide.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.chat.dto.ChatMessageType;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.userrepo.UserRepo;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userrepo_id")
    private UserRepo userRepo;

    @Column(length = 255, nullable = false)
    private String messageContent;

    private ChatMessageType chatMessageType;

    private Long userId;
    private String repoId;
}
