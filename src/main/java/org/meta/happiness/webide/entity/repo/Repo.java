package org.meta.happiness.webide.entity.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.meta.happiness.webide.dto.repo.RepoCreateRequest;
import org.meta.happiness.webide.entity.BaseTimeEntity;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Repo extends BaseTimeEntity {

    @Id
    @Column(name = "repo_id")
    @GeneratedValue
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @Enumerated(EnumType.STRING)
    private Language programmingLanguage;

    private String name;

    private String efsAccessPoint;

    public static Repo createRepo(RepoCreateRequest request, User creator) {
        Repo repo = new Repo();
        repo.creator =creator;
        repo.programmingLanguage = request.getProgrammingLanguage();
        repo.name = request.getName();
        return repo;
    }

    public void setName(String name){
        this.name = name;
    }

//    public void setAccessPointId(String accessPointId) {
//        this.efsAccessPoint = accessPointId;
//    }
}
