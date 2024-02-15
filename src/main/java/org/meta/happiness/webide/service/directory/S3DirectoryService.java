package org.meta.happiness.webide.service.directory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.repository.directory.S3DirectoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class S3DirectoryService {

    private final S3DirectoryRepository repository;

    public static final String DELIMITER = "/";

    @Transactional
    public void createDirectory(String repoid, String directoryPath) {
        String s3Path = "repo" + DELIMITER + repoid + DELIMITER + directoryPath;

        if (repository.isDirectoryExist(s3Path)){
            throw new IllegalArgumentException("Directory가 이미 존재함");
        }
        repository.putDirectoryPath(s3Path);
    }

    public void deleteDirectory(String repo, String directoryPath) {
        String s3Path = "repo" + DELIMITER + repo + DELIMITER + directoryPath;

        if (!repository.isDirectoryExist(s3Path)){
            throw new IllegalArgumentException("Directory가 없음");
        }

        repository.deleteDirectoryPath(s3Path);
    }
}
