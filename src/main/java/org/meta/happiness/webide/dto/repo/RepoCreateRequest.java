package org.meta.happiness.webide.dto.repo;

import lombok.Getter;
import org.meta.happiness.webide.entity.repo.Language;

@Getter
public class RepoCreateRequestDto {
    private String name;
    private Language programmingLanguage;
}
