package org.meta.happiness.webide.dto.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Language;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoResponseDto {
    private String id;
    private User createId;
    private String name;
    private Language programmingLanguage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static RepoResponseDto convertRepoToDto(Repo repo){
        return new RepoResponseDto(repo.getId(),
                repo.getCreator(),
                repo.getName(),
                repo.getProgrammingLanguage(),
                repo.getCreatedDate(),
                repo.getLastModifiedDate());
    }
}
