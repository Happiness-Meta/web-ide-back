package org.meta.happiness.webide.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateFileRequest {
    private String originFilepath;
    private String newFilepath;
    private String content;
}
