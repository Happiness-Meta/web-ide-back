package org.meta.happiness.webide.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private String content;
    private String repoId;
    private Long userId;
    private String sender;
    private ChatMessageType type;
    private LocalDateTime createdAt;
}

