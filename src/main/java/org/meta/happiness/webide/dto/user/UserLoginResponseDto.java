package org.meta.happiness.webide.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLoginResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String token;
}

