package org.meta.happiness.webide.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatUserInfoDto {
    private String email;
    private String userName;

    @Builder
    public ChatUserInfoDto(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }
}
