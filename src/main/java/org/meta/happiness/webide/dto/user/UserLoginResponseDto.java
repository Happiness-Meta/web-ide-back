package org.meta.happiness.webide.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLoginResponseDto {
    private String email;
    private String nickname;
    private String token;
}

