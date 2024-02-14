package org.meta.happiness.webide.entity.userrepo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

@Entity
@Getter
@NoArgsConstructor
public class UserRepo {

    @Id
    @Column(name = "userrepo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repo_id")
    private Repo repo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static UserRepo addUserRepo(Repo repo, User user){
        UserRepo userRepo = new UserRepo();
        userRepo.repo = repo;
        userRepo.user = user;
        return userRepo;
    }
}
