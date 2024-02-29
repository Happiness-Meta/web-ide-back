package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.common.exception.FileMetaDataPathException;
import org.meta.happiness.webide.common.exception.RepoNotFoundException;
import org.meta.happiness.webide.dto.file.UpdateFileRequest;
import org.meta.happiness.webide.entity.FileMetaData;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.repository.filemetadata.FileMetaDataRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.meta.happiness.webide.service.filemetadata.FileMetaDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final RepoRepository repoRepository;
    private final FileMetaDataService fileMetaDataService;
    private final FileMetaDataRepository fileMetaDataRepository;
    private final S3FileService s3fileService;

    @Transactional
    public void createFile(String repoId, String filePath) {
        Repo repo = findRepoById(repoId);
        fileMetaDataService.setPath(repo, filePath);
        FileMetaData fileMetaData = getFileMetaData(repo, filePath);

        createS3FilePath(repo.getId(), fileMetaData.getId());
        log.info("파일이 생성되었습니다: {}", filePath);
    }

    @Transactional
    public void updateFile(String repoId, UpdateFileRequest request) {
        Repo repo = findRepoById(repoId);
        FileMetaData fileMetaData = getFileMetaData(repo, request.getOriginFilepath());
        fileMetaData = fileMetaData.changePath(request.getNewFilepath());

        updateS3File(repo.getId(), fileMetaData.getId(), request.getContent());
        log.info("파일이 업데이트되었습니다: {} -> {}", request.getOriginFilepath(), request.getNewFilepath());
    }

    @Transactional
    public void deleteFile(String repoId, String filePath) {
        Repo repo = findRepoById(repoId);
        FileMetaData fileMetaData = getFileMetaData(repo, filePath);

        deleteS3File(repo.getId(), fileMetaData.getId());
        log.info("파일이 삭제되었습니다: {}", filePath);
    }

    private Repo findRepoById(String repoId) {
        return repoRepository.findById(repoId)
                .orElseThrow(() -> new RepoNotFoundException("Repo를 찾을 수 없음"));
    }

    private FileMetaData getFileMetaData(Repo repo, String filePath) {
        return fileMetaDataRepository.findByRepoAndPath(repo, filePath)
                .orElseThrow(() -> new FileMetaDataPathException("파일 메타데이터를 찾을 수 없음"));
    }

    private void createS3FilePath(String repoId, String fileMetaDataId) {
        s3fileService.createFilePath(repoId, fileMetaDataId);
    }

    private void updateS3File(String repoId, String fileMetaDataId, String content) {
        s3fileService.updateFile(repoId, fileMetaDataId, content);
    }

    private void deleteS3File(String repoId, String fileMetaDataId) {
        s3fileService.deleteFile(repoId, fileMetaDataId);
    }
}