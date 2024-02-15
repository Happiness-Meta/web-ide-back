package org.meta.happiness.webide.service.directory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.directory.CreateDirectoryRequestDto;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectoryService {

    public final RepoRepository repoRepository;
    public final S3DirectoryService s3DirectoryService;

    public void createDirectory(String repoId, String directoryPath) {
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 레포지토리입니다."));

        s3DirectoryService.createDirectory(repo.getId(), directoryPath);


    }
}
