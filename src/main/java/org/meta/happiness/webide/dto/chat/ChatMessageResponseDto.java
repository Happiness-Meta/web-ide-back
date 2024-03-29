package org.meta.happiness.webide.dto.chat;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {
    String sender;
    String content;
    ChatMessageType type;
}
