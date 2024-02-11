package org.meta.happiness.webide.entity.groupuser;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

@Entity
@Getter
@NoArgsConstructor
public class GroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    private Repo repo;

    @ManyToOne
    private User user;

    public GroupUser (Repo repo, User user){
        this.repo = repo;
        this.user = user;
    }
}
