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
//            @PathVariable String projectId
            Long parentId, String parentCode,
            @RequestPart("files") MultipartFile[] files
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
    @Operation(summary = "파일 수정", description = "특정 파일, 특정 디렉토리가 한번에 추가되거나 삭제되어도 저장될 때 put으로 업데이트되면 한번에 변경될 것")
    public ApiResponse<?> modifyFiles(
//            @PathVariable String projectId
    ) {

        return ApiResponse.ok();
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "파일 삭제", description = "")
    public ResponseEntity<?> deleteFiles(String filePath) {
//        return fileComponent.delete(filePath);
        return null;
    }
}
