package org.meta.happiness.webide.repository.groupuser;

import org.meta.happiness.webide.entity.groupuser.GroupUser;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    Optional<GroupUser> findByUserAndRepo(User user, Repo repo);
}
