package org.meta.happiness.webide.controller.directory;


import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.directory.CreateDirectoryRequestDto;
import org.meta.happiness.webide.dto.directory.DeleteDirectoryRequestDto;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.directory.DirectoryService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directories")
public class DirectoryController {

    private final ResponseService responseService;

    private final DirectoryService directoryService;


    @PostMapping("/{repoId}")
    public SingleResult<String> createDirectory(
            @PathVariable("repoId") String repoId,
//            @RequestParam("directoryPath") String directoryPath
            @RequestBody CreateDirectoryRequestDto request
    ) {
        directoryService.createDirectory(repoId, request.getDirectoryPath());
        return responseService.handleSingleResult("Sucess");

    }

//    @DeleteMapping("/{repoId}")
    @PostMapping("/delete-dir/{repoId}")
    public SingleResult<?> deleteDirectory(
            @PathVariable("repoId") String repoId,
//            @RequestParam("directoryPath") String directoryPath
            @RequestBody DeleteDirectoryRequestDto request
    ) {
        directoryService.deleteDirectory(repoId, request.getDirectoryPath());
        return responseService.handleSingleResult("Sucess");
    }

    @PutMapping("/{repoId}")
    public SingleResult<?> updateDirectoryName(
            @PathVariable("repoId") String repoId,
            @RequestParam("dirPath") String directoryPath,
            @RequestParam("newName") String newName
    ) {
        directoryService.updateDirectoryName(repoId, directoryPath, newName);
        return responseService.handleSingleResult("Sucess");
    }
}
