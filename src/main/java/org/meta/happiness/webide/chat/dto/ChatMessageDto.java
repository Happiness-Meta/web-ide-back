package org.meta.happiness.webide.chat.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private ChatMessageType type;
    private String content;
    private String sender;
}

