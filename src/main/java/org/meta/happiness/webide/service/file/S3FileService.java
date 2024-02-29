package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.common.exception.FileAlreadyExistsException;
import org.meta.happiness.webide.common.exception.FileNotFoundException;
import org.meta.happiness.webide.repository.file.S3FileRepository;
import org.springframework.stereotype.Service;

/**
 * S3에 저장시
 * => repo/{repoId}/{uuid}.txt의 형태로 저장
 * metadata의 경우 데이터베이스에서 저장
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService {

    private final S3FileRepository repository;
    public static final String DELIMITER = "/";

    public void createFilePath(String repoId, String filepath) {
        String s3Path = constructS3Path(repoId, filepath);
        if (repository.isFileExist(s3Path)) {
            throw new FileAlreadyExistsException("파일이 이미 존재합니다: " + s3Path);
        }
        repository.putFilePath(s3Path);
    }

    public void updateFile(String repoId, String filePath, String content) {
        String s3Path = constructS3Path(repoId, filePath);
        if (!repository.isFileExist(s3Path)) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + s3Path);
        }
        repository.putFile(s3Path, content);
    }

    public void deleteFile(String repoId, String filePath) {
        String s3Path = constructS3Path(repoId, filePath);
        if (!repository.isFileExist(s3Path)) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + s3Path);
        }
        repository.deleteFile(s3Path);
    }

    private String constructS3Path(String repoId, String filePath) {
        return "repo" + DELIMITER + repoId + DELIMITER + filePath;
    }
}