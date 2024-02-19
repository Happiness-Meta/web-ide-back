package org.meta.happiness.webide.dto.file;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileDto {
    private String uuid;
    private String filePath;
    private String content;

    @Builder
    public FileDto(String uuid, String filePath, String content) {
        this.uuid = uuid;
        this.filePath = filePath;
        this.content = content;
    }
}
