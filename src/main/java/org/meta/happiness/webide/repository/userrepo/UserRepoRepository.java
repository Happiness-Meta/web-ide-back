package org.meta.happiness.webide.repository.userrepo;

import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepoRepository extends JpaRepository<UserRepo, Long> {
    Optional<UserRepo> findByUserAndRepo(User user, Repo repo);
}
