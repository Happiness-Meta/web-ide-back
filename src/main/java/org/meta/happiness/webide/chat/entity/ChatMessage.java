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
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 영속성 컨텍스트에서 select문을 미리 뽑아야 하는데, 이 친구가 진짜 필요한 시점에만 사용하기 위해서 lazy를 사용 하는 것.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userrepo_id")
    private UserRepo userRepo;

    @Column(length = 255, nullable = false)
    private String messageContent;

    private ChatMessageType chatMessageType;

    private Long userId;
    private String repoId;
}
