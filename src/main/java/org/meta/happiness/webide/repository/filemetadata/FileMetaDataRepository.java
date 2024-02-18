package org.meta.happiness.webide.repository.filemetadata;

import org.meta.happiness.webide.entity.FileMetaData;
import org.meta.happiness.webide.entity.repo.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, String> {
    Optional<FileMetaData> findByPath(String path);

    Optional<FileMetaData> findByRepoAndPath(Repo repo, String path);
}
