package org.meta.happiness.webide.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @Email(message = "email형식에 맞게 아이디를 입력해주세요.")
    private String email;
    @NotBlank(message = "닉네임은 필수 입력란입니다.")
    @Length(max = 10, message = "닉네임 길이는 최대 10자까지 가능합니다.")
    @Pattern (regexp = "^[a-zA-Z0-0]*$", message = "닉네임은 영어랑 숫자만 가능합니다.")
    private String nickname;
    @Pattern (regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩" +
                    " 포함된 8 ~20자의 비밀번호여야 합니다.")
    private String password;
}
