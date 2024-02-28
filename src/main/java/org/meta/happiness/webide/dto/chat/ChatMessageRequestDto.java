package org.meta.happiness.webide.dto.chat;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // 아래 모든 필드를 사용해야 함
@NoArgsConstructor // todo: 빈 생성자가 있어야 하는 이유는?
public class ChatMessageRequestDto {
    @NotBlank(message = "메시지 보내는 이가 있어야 합니다.")
    String sender;
    @NotBlank(message = "메시지 내용이 있어야 합니다.")
    String content;
    @NotBlank(message = "메시지 타입이 있어야 합니다.")
    ChatMessageType type;
    @NotBlank(message = "레포 아이디가 있어야 합니다.")
    String repoId;
}
