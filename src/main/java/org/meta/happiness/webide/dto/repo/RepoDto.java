package org.meta.happiness.webide.dto.repo;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.meta.happiness.webide.entity.repo.Language;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RepoDto {

    public String id;

    private String name;

    private String language;

    private LocalDateTime createAt;

    private LocalDateTime modifiedAt;


    public static RepoDto of(Repo repo){
        return new RepoDto(
                repo.getId(),
                repo.getName(),
                repo.getProgrammingLanguage().toString(),
                repo.getCreatedAt(),
                repo.getModifiedAt()
        );
    }
}
