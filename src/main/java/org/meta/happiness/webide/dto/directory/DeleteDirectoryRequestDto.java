package org.meta.happiness.webide.dto.directory;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteDirectoryRequestDto {
    private String directoryPath;
}
