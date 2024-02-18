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
    private String repoId;
    private String sender;
    private LocalDateTime createdAt;
}

