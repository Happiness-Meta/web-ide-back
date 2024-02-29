package org.meta.happiness.webide.service.repo;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.common.exception.S3UploadException;
import org.meta.happiness.webide.repository.repo.S3RepoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3RepoService {

    private final S3RepoRepository repository;

    public void createRepository(String repoName) {
        repository.uploadRepo(repoName).orElseThrow(S3UploadException::new);
    }

    public void deleteRepository(String repositoryPath) {
        repository.deleteRepoWithRepoPath(repositoryPath);
    }
}
