package org.meta.happiness.webide.entity.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.user.User;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Repo extends BaseTimeEntity {

    @Id
    @Column(name = "repo_id")
    public String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    private Language programmingLanguage;

    private String name;

    private String efsAccessPoint;

    public static Repo createRepo(RepoCreateRequestDto request, User creator) {
        Repo repo = new Repo();
        repo.id = UUID.randomUUID().toString();
        repo.creator = creator;
        repo.programmingLanguage = request.getProgrammingLanguage();
        repo.name = request.getName();
        return repo;
    }

    public void setName(String name){
        this.name = name;
    }

    public void createAccessPointId(String accessPointId) {
        this.efsAccessPoint = accessPointId;
    }
}
