package org.meta.happiness.webide.dto.repo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Language;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepoTemplateCreateRequestDto {
    @NotBlank(message = "레포 이름은 필수 입력란입니다.")
    private String name;
}