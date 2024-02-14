package org.meta.happiness.webide.controller.directory;


import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.directory.CreateDirectoryRequest;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.service.ResponseService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
public class DirectoryController {

    private final ResponseService responseService;


    @PostMapping("/{repoId}")
    public SingleResult<Void> createDirectory(
            @PathVariable("repoId") String repoId,
            @RequestParam("directoryPath") String directoryPath,
            @RequestBody CreateDirectoryRequest request
    ) {
//        directoryService.createDirectory(containerId, directoryPath, request);
//        return responseService.handleSingleResult();
    }

    @DeleteMapping("/{repoId}")
    public SingleResult<Void> deleteDirectory(
            @PathVariable("repoId") String repoId,
            @RequestParam("directoryPath") String directoryPath
    ) {
//        directoryService.deleteDirectory(repoId, directoryPath);
//        return responseService.handleSingleResult();
        return null;
    }

    @PutMapping("/{repoId}")
    public SingleResult<Void> updateDirectoryName(
            @PathVariable("repoId") String repoId,
            @RequestParam("dirPath") String directoryPath,
            @RequestParam("newName") String newName
    ) {
//        directoryService.updateDirectoryName(containerId, directoryPath, request);
//        return responseService.handleSingleResult();
        return null;
    }
}
