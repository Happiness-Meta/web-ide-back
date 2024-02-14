package org.meta.happiness.webide.controller.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/files")
@Tag(name = "File & Directory", description = "파일과 디렉토리 관련 API")
public class FileController {

    @PostMapping("/{repoId}")
    @Operation(summary = "파일 생성", description = "")
    public ApiResponse<?> createFiles(
            @PathVariable("repoId") String repoId,
            @RequestParam("filePath") String filePath
//            @RequestBody CreateFileRequest request
//            @RequestPart("files") MultipartFile[] files
    ) {


        return ApiResponse.ok();
    }

    @GetMapping("/{repoId}")
    @Operation(summary = "파일 불러오기", description = "")
    public ResponseEntity<byte[]> getFiles(String fileUrl) throws IOException {
//        String filePath = fileUrl.substring(52);
//        return fileComponent.download(filePath);

        return null;
    }

    @PutMapping("/{repoId}")
    @Operation(summary = "파일 수정", description = "")
    public ApiResponse<?> modifyFiles(
            @PathVariable("repoId") String repoId,
            @RequestParam("filePath") String filePath
//            @RequestBody UpdateFileRequest request
    ) {
//        fileService.updateFile(repoId, filePath, request);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "파일 삭제", description = "")
    public ResponseEntity<?> deleteFiles(
            @PathVariable("repoId") String repoId,
            @RequestParam("filePath") String filePath
    ) {
//        fileService.deleteFile(repoId, filePath);
        return null;
    }
}
