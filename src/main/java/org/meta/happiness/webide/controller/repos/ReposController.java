package org.meta.happiness.webide.controller.repos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.meta.happiness.webide.repostarter.RepoStarter;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 수연님 코멘트
 * -레포지토리 이름
 * -레포지토리 설명(디스크립션)
 * -레포지토리 마지막 편집일
 * -레포지토리 url??은 어느쪽에서 만들 수 있는 건가요
 * -레포지토리에 알맞는 image url(누르면 해당 레포지토리에 해당하는 코드 페이지로 이동하는)
 *
 */
@RestController
@RequestMapping("/api/repos")
@Tag(name = "Repository", description = "레포지토리 관련 API")
public class ReposController {

    private final RepoStarter repoStarter;

    public ReposController(RepoStarter repoStarter) {
        this.repoStarter = repoStarter;
    }

    @GetMapping
    public ApiResponse<?> createTest(
    ) {

        repoStarter.startRepository();

        return ApiResponse.ok();
    }

    @GetMapping("/{userId}/all")
    @Operation(summary = "사용자 전체 레포지토리 조회", description = "")
    public ApiResponse<?> getAllRepositoryByUser(
//            @PathVariable String projectId
    ) {
        return ApiResponse.ok();
    }

    @GetMapping("/{userId}/recent")
    @Operation(summary = "사용자가 최근 사용한 레포지토리 조회(최대 2개)", description = "")
    public ApiResponse<?> getRecentRepository(
//            @PathVariable String projectId
    ) {
        return ApiResponse.ok();
    }

    @GetMapping("/{repoId}")
    @Operation(summary = "개별 레포지토리 조회", description = "")
    public ApiResponse<?> getRepository(
//            @PathVariable String projectId
    ) {

        return ApiResponse.ok();
    }

    @PostMapping
    @Operation(summary = "신규 레포지토리 생성", description = "")
    public ApiResponse<?> createRepository(
//            @RequestBody ProjectCreateRequestDto projectCreateRequestDto,
//            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.ok();
    }

    @PatchMapping("/{repoId}")
    @Operation(summary = "레포지토리 이름 변경", description = "")
    public ApiResponse<Void> updateRepositoryName(
//            @AuthenticationPrincipal User user,
//            @RequestBody RepoUpdateRequest request
    ) {
        return ApiResponse.ok();
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "레포지토리 삭제", description = "")
    public ApiResponse<Void> deleteRepository(
//            @PathVariable String projectId,
//            @AuthenticationPrincipal User user
    ) {

        return ApiResponse.ok();
    }

}
