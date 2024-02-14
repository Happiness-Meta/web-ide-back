package org.meta.happiness.webide.controller.repos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.dto.repo.RepoResponseDto;
import org.meta.happiness.webide.dto.repo.RepoUpdateNameRequestDto;
import org.meta.happiness.webide.dto.response.MultipleResult;
import org.meta.happiness.webide.dto.response.Result;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.dto.user.UserResponseDto;


import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.meta.happiness.webide.repostarter.RepoStarter;

import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.repo.RepoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 수연님 코멘트
 * -레포지토리 이름
 * -레포지토리 설명(디스크립션)
 * -레포지토리 마지막 편집일
 * -레포지토리 url??은 어느쪽에서 만들 수 있는 건가요
 * -레포지토리에 알맞는 image url(누르면 해당 레포지토리에 해당하는 코드 페이지로 이동하는)
 */
// TODO: User 권한 검사 필요합니다.
@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
@Tag(name = "Repository", description = "레포지토리 관련 API")
public class ReposController {

    private final RepoService repoService;
    private final ResponseService responseService;


    @PostMapping
    @Operation(summary = "신규 레포지토리 생성", description = "")
    public SingleResult<RepoResponseDto> createRepository(
            @RequestBody RepoCreateRequestDto request,
            @AuthenticationPrincipal User user) {
        return responseService.handleSingleResult(repoService.createRepository(request, user));
    }

//TODO: 지금은 creator만 조회가 가능하다.
    @GetMapping("/{userId}/{repoId}")
    @Operation(summary = "개별 레포지토리 조회", description = "")
    public SingleResult<RepoResponseDto> getRepository(
            @PathVariable Long userId,
            @PathVariable String repoId
    ) {

        return responseService.handleSingleResult(repoService.findRepo(repoId, userId));
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


    @PatchMapping("/{repoId}")
    @Operation(summary = "레포지토리 이름 변경", description = "")
    public SingleResult<RepoResponseDto> updateRepositoryName(
            @PathVariable("repoId") String repoId,
            @AuthenticationPrincipal User user,
            @RequestBody RepoUpdateNameRequestDto request
    ) {
        return responseService.handleSingleResult(repoService.updateRepositoryName(repoId, request, user));
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "레포지토리 삭제", description = "")
    public Result deleteRepository(
            @PathVariable("repoId") String repoId,
            @AuthenticationPrincipal User user
    ) {
        repoService.deleteRepository(repoId, user);
        return responseService.handleSuccessResult();

    }


    @GetMapping("/{userId}/all")
    @Operation(summary = "사용자 전체 레포지토리 조회", description = "")
    public MultipleResult<RepoResponseDto> getAllRepositoryByUser(
            @PathVariable Long userId
    ) {
        return responseService.handleListResult(repoService.findAllRepoByUser(userId));
    }


//
//    @GetMapping("/{userId}/recent")
//    @Operation(summary = "사용자가 최근 사용한 레포지토리 조회(최대 2개)", description = "")
//    public MultipleResult<RepoResponseDto> getRecentRepository(
//            @PathVariable Long userId
//    ) {
//        Pageable pageable = PageRequest.of(0, 2, Sort.by("createdDate").descending());
//        return responseService.handleListResult(repoService.findRecentRepoByUser(userId));
//
//    }
}
