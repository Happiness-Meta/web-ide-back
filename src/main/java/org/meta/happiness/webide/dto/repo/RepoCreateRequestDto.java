package org.meta.happiness.webide.dto.repo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Language;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepoCreateRequestDto {
    @NotBlank(message = "레포 이름은 필수 입력란입니다.")
    private String name;
    @NotBlank(message = "레포의 언어 설정은 필수 입니다.")
    private Language programmingLanguage;
//    @NotBlank(message = "레포의 비밀번호는 필수 입니다.")
//    private String password;
}
