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
    // todo: 외래 키를 두개 가져와서 사용하는 경우, 발생 가능한 오류 체크
    //    @Column(nullable = false, unique = true)
    @ManyToOne(fetch = FetchType.LAZY)
    // todo: 프론트에 닉네임 어떻게 넘겨야 하는지 고민 -> 이미 로그인 상태라서 고민할 필요 없음
    @JoinColumn(name = "userrepo_id")
    private UserRepo userRepo;
    @Column(length = 255, nullable = false)
    private String messageContent;
    //todo: enum 타입의 경우 어떤 설정을 해줘야 하는가?
    private ChatMessageType chatMessageType;

}
