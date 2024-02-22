package org.meta.happiness.webide.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.file.RunFileRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunService {
    private final CompilerService compilerService;

    @Transactional
    public String inputCompile(RunFileRequest request) {
        log.info("{}", request.toString());
        try {
            String result = compilerService.executeCode(request.getCode());
            log.info(result);
            return result;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("뭔가 잘못됨");
        }
    }
}
