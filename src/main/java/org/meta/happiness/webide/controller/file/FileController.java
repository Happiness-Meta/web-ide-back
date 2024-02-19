package org.meta.happiness.webide.controller.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.file.CreateFileRequest;
import org.meta.happiness.webide.dto.file.UpdateFileRequest;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.file.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File & Directory", description = "파일과 디렉토리 관련 API")
public class FileController {

    private final ResponseService responseService;
    private final FileService fileService;

    @PostMapping("/{repoId}")
    @Operation(summary = "파일 생성", description = "")
    public SingleResult<String> createFiles(
            @PathVariable("repoId") String repoId,
            @RequestBody CreateFileRequest request
//            @RequestPart("files") MultipartFile[] files
    ) {
        fileService.createFile(repoId, request.getFilepath());
        return responseService.handleSingleResult("Sucess");
    }

    @GetMapping("/{repoId}")
    @Operation(summary = "파일 불러오기", description = "")
    public ResponseEntity<byte[]> getFiles(String fileUrl) throws IOException {
//        String filePath = fileUrl.substring(52);
//        return fileComponent.download(filePath);

        return null;
    }

    @PutMapping("/{repoId}")
    @Operation(summary = "파일 업데이트", description = "")
    public SingleResult<String> modifyFiles(
            @PathVariable("repoId") String repoId,
            @RequestBody UpdateFileRequest request
    ) {
        fileService.updateFile(repoId, request);
        return responseService.handleSingleResult("Sucess");
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "파일 삭제", description = "")
    public SingleResult<String> deleteFiles(
            @PathVariable("repoId") String repoId,
            @RequestParam("filePath") String filePath
    ) {
        fileService.deleteFile(repoId, filePath);
        return responseService.handleSingleResult("Sucess");
    }
}
