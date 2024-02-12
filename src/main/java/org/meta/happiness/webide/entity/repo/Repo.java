package org.meta.happiness.webide.entity.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.meta.happiness.webide.entity.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Repo {

    @Id
    public String id;

    @ManyToOne
    private User creator;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Enumerated(EnumType.STRING)
    private Language programmingLanguage;

    private String name;

    private String efsAccessPoint;

    public static Repo createRepo(String name, Language programmingLanguage, User creator) {
        Repo repo = new Repo();
        repo.id = UUID.randomUUID().toString();
        repo.creator = creator;
        repo.programmingLanguage = programmingLanguage;
        repo.name = name;
        return repo;
    }

    public void setName(String name){
        this.name = name;
    }

//    public void setAccessPointId(String accessPointId) {
//        this.efsAccessPoint = accessPointId;
//    }
}
