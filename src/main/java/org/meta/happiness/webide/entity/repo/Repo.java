package org.meta.happiness.webide.entity.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.entity.userrepo.UserRepo;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "repos")
public class Repo extends BaseTimeEntity {

    @Id
    @Column(name = "repo_id")
    public String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    private Language programmingLanguage;

    private String name;

    private String efsAccessPoint;

    @OneToMany(mappedBy = "repo", cascade = CascadeType.REMOVE)
    private List<UserRepo> userRepoUsers;

    public static Repo createRepo(RepoCreateRequestDto request, User creator) {
        Repo repo = new Repo();
        repo.id = UUID.randomUUID().toString();
        repo.creator = creator;
        repo.programmingLanguage = request.getProgrammingLanguage();
        repo.name = request.getName();
        return repo;
    }

    public void changeName(String name){
        this.name = name;
    }

    public void createAccessPointId(String accessPointId) {
        this.efsAccessPoint = accessPointId;
    }
}
