package org.meta.happiness.webide.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.meta.happiness.webide.dto.file.FileDto;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepoGetAllFilesResponse {
    private RepoTreeResponse treeData;
    private List<FileDto> fileData;

    @Builder
    public RepoGetAllFilesResponse(
            RepoTreeResponse treeData,
            List<FileDto> fileData
    ) {
        this.treeData = treeData;
        this.fileData = fileData;
    }
}
