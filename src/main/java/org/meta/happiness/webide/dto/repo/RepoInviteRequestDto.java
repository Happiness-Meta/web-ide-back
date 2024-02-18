package org.meta.happiness.webide.dto.repo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoInviteRequestDto {
    @NotBlank(message = "해당 레포의 비밀번호를 입력해 주세요.")
    private String password;
}
