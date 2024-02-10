package org.meta.happiness.webide.entity.groupuser;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;

@Entity
@Getter
@NoArgsConstructor
public class GroupUser {

    @Id
    public Long id;

    @ManyToOne
    private Repo repo;

    @ManyToOne
    private User user;
}
