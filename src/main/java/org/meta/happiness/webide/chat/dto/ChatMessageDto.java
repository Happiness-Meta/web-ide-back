package org.meta.happiness.webide.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private ChatMessageType type;
    private String content;
    //todo: 레포 id가 왜 문자열이냐??? 이거 확인 필요. 레포 이름이랑 아이디는 따로 가져가야지.
    private String repoId;
    private String sender;
    private LocalDateTime createdAt;
}

