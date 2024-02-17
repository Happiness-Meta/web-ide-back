package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.repository.file.S3FileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService {

    private final S3FileRepository repository;
    public static final String DELIMITER = "/";

    public void createFilePath(String repoId, String filepath) {
        String s3Path = "repo" + DELIMITER + repoId + DELIMITER + filepath + ".txt";
        if (repository.isFileExist(s3Path)) {
            throw new IllegalArgumentException("File이 이미 존재함");
        }
        repository.putFilePath(s3Path);
    }

    public void updateFile(String repoId, String filePath, String content) {
        String s3Path = "repo" + DELIMITER + repoId + DELIMITER + filePath + ".txt";
        if (!repository.isFileExist(s3Path)) {
            throw new IllegalArgumentException("File이 존재하지 않음");
        }
        repository.putFile(s3Path, content);
    }

    public void deleteFile(String repoId, String filePath) {
        String s3Path = "repo" + DELIMITER + repoId + DELIMITER + filePath + ".txt";
        if (!repository.isFileExist(s3Path)) {
            throw new IllegalArgumentException("File이 존재하지 않음");
        }
        repository.deleteFile(s3Path);
    }
}
