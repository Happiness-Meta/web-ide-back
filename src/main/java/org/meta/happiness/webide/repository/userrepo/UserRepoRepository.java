package org.meta.happiness.webide.repository.userrepo;

import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepoRepository extends JpaRepository<UserRepo, Long> {
    List<UserRepo> findByUser(User user);

    Optional<UserRepo> findByUserAndRepo(User user, Repo repo);

    @Query("select r from Repo r join r.userRepoUsers ur where ur.user = : user")
    List<UserRepo> findAllRepoByUser(@Param("user") Optional<User> user);


//    @Query(value = "SELECT * FROM user_repo ORDER BY repo_last_modified_date DESC LIMIT 2", nativeQuery = true)
//    List<UserRepo> findRecentlyRepo();
}
