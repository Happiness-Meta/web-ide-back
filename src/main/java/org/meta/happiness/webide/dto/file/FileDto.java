package org.meta.happiness.webide.dto.file;

import lombok.Builder;

public class FileDto {
    private String filePath;
    private String content;

    @Builder
    public FileDto(String filePath, String content) {
        this.filePath = filePath;
        this.content = content;
    }
}
