package org.meta.happiness.webide.repository.repo;

import org.meta.happiness.webide.entity.repo.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoRepository extends JpaRepository<Repo, String> {

}
