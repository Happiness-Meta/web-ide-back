package org.meta.happiness.webide.dto.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.user.UserCreatorDto;
import org.meta.happiness.webide.entity.repo.Language;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepoResponseDto {
    private String id;
    private UserCreatorDto creatorDto;
    private String name;
    private Language programmingLanguage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static RepoResponseDto convertRepoToDto(Repo repo){
        return new RepoResponseDto(repo.getId(),
                new UserCreatorDto(repo.getCreator().getEmail()),
                repo.getName(),
                repo.getProgrammingLanguage(),
                repo.getCreatedDate(),
                repo.getLastModifiedDate());
    }
}
