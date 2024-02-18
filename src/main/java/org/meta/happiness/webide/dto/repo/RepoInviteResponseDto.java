package org.meta.happiness.webide.dto.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Repo;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepoInviteResponseDto {
    private String repoUrl;
    private String repoPassword;

    public static RepoInviteResponseDto convertRepoToInvite(Repo repo){
        return new RepoInviteResponseDto("http://localhost:5173/dashboard/codePage/" + repo.getId(),
                repo.getPassword());
    }
}