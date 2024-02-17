package org.meta.happiness.webide.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.entity.repo.Repo;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMetaData {

    @Id
    @Column(name = "filemetadata_id")
    private String id;

    private String path;

    @ManyToOne
    @JoinColumn(name = "repo_id")
    private Repo repo;


    @Builder
    public FileMetaData(String path, Repo repo){
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.repo = repo;
    }

    @Transactional
    public FileMetaData changePath(String newPath){
        this.path = newPath;
        return this;
    }
}
