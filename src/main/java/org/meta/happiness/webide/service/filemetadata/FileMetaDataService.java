package org.meta.happiness.webide.service.filemetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.common.exception.FileAlreadyExistsException;
import org.meta.happiness.webide.common.exception.FileMetaDataNotFoundException;
import org.meta.happiness.webide.common.exception.FileNotFoundException;
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
        if (repository.findByPath(filepath).isPresent()) {
            // 파일이 이미 존재하면 예외 발생
            throw new FileAlreadyExistsException("File already exists: " + filepath);
        }

        repository.save(
                FileMetaData.builder()
                        .repo(repo)
                        .path(filepath)
                        .build()
        );
    }

    public void deletePath(Repo repo, String filepath) {
        FileMetaData fileMetaData = repository.findByPath(filepath)
                .orElseThrow(FileMetaDataNotFoundException::new);
        repository.delete(fileMetaData);
    }
}
