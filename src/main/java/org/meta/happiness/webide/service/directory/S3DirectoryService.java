package org.meta.happiness.webide.service.directory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.repository.directory.S3DirectoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;


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

    public void updateDirectoryName(String repo, String directoryPath, String newDirectoryName) {
        String originS3Path = "repo" + DELIMITER + repo + DELIMITER + directoryPath;
        if (!repository.isDirectoryExist(originS3Path)){
            log.error(">>>>>>> repo에 해당 디렉 없음");
            throw new IllegalArgumentException("Directory가 없음");
        }

        String temp = directoryPath.substring(0, directoryPath.length() - 1);
        int secondLastDelimiterIndex =  temp.lastIndexOf(DELIMITER);
        String newPath = originS3Path.substring(0, secondLastDelimiterIndex) + DELIMITER + newDirectoryName + DELIMITER;

        List<S3Object> objects = repository.getObjectsBy(originS3Path);
        for (S3Object s3Object : objects) {

            String newKey = newPath + s3Object.key().substring(originS3Path.length());
            repository.copyObject(s3Object, newKey);
            repository.deleteObject(s3Object);
        }

    }
}
