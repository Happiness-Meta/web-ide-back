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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    private Repo repo;

    @ManyToOne
    private User user;

    public UserRepo(Repo repo, User user){
        this.repo = repo;
        this.user = user;
    }
}