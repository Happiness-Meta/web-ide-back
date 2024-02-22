package org.meta.happiness.webide.controller.file;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.file.RunFileRequest;
import org.meta.happiness.webide.dto.response.Result;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.file.RunService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/run")
public class RunController {
    private final RunService runService;
    private final ResponseService responseService;

    @PostMapping
    public SingleResult<?> runCode(@RequestBody RunFileRequest runFileRequest){
        return responseService.handleSingleResult(runService.inputCompile(runFileRequest));
    }
}
