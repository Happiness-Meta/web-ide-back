package org.meta.happiness.webide.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {
    String sender;
    String content;
    ChatMessageType type;
}
