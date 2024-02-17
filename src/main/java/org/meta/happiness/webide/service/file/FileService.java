package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.file.UpdateFileRequest;
import org.meta.happiness.webide.entity.FileMetaData;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.repository.filemetadata.FileMetaDataRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.meta.happiness.webide.service.filemetadata.FileMetaDataService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final RepoRepository repoRepository;

    private final FileMetaDataService fileMetaDataService;
    private final FileMetaDataRepository fileMetaDataRepository;

    private final S3FileService s3fileService;

    public void createFile(String repoId, String filePath) {
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레포지토리입니다."));

        fileMetaDataService.setPath(repo, filePath);

        FileMetaData fileMetaData = fileMetaDataRepository.findByRepoAndPath(repo, filePath)
                .orElseThrow(() -> new IllegalArgumentException("path 오류"));
        log.info("saved file path >>>>> {}", fileMetaData.getPath());

        s3fileService.createFilePath(repo.getId(), fileMetaData.getId());


    }

    public void updateFile(String repoId, UpdateFileRequest request) {
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레포지토리입니다."));

        FileMetaData fileMetaData = fileMetaDataRepository.findByRepoAndPath(repo, request.getOriginFilepath())
                .orElseThrow(() -> new IllegalArgumentException("path 오류"));
        fileMetaData.changePath(request.getOriginFilepath(), request.getNewFilepath());

        log.info("update file originPath >>>>> {}", request.getOriginFilepath());
        log.info("update file newPath >>>>> {}", fileMetaData.getPath());

        s3fileService.updateFile(repo.getId(), fileMetaData.getId(), request.getContent());
    }

    public void deleteFile(String repoId, String filePath) {
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레포지토리입니다."));

        FileMetaData fileMetaData = fileMetaDataRepository.findByRepoAndPath(repo, filePath)
                .orElseThrow(() -> new IllegalArgumentException("path 오류"));
        log.info("delete file path >>>>> {}", fileMetaData.getPath());

        fileMetaDataService.deletePath(repo, filePath);
        s3fileService.deleteFile(repo.getId(), fileMetaData.getId());
    }
}
