package org.meta.happiness.webide.service.repo;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.repository.repo.S3RepoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3RepoService {

    private final S3RepoRepository repository;

    public String createRepository(String repoName) {
        return repository.uploadRepo(repoName).orElseThrow(() -> new IllegalArgumentException("S3 container Upload Exception"));
    }
}
