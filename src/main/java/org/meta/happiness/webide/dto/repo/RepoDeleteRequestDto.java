package org.meta.happiness.webide.dto.repo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Language;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepoDeleteRequestDto {
    private String userEmail;
}