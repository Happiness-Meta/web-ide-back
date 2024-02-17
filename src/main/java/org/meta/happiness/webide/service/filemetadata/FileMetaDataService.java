package org.meta.happiness.webide.service.filemetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.entity.FileMetaData;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.repository.filemetadata.FileMetaDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileMetaDataService {
    private final FileMetaDataRepository repository;

    public void setPath(Repo repo, String filepath) {
        repository.save(
                FileMetaData.builder()
                        .repo(repo)
                        .path(filepath)
                        .build()
        );
    }

    public void deletePath(Repo repo, String filepath) {
        FileMetaData fileMetaData = repository.findByPath(filepath)
                .orElseThrow(() -> new IllegalArgumentException("파일 정보 없음"));
        repository.delete(fileMetaData);
    }
}
