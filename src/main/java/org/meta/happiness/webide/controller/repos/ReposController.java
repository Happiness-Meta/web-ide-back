package org.meta.happiness.webide.controller.repos;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.api.ApiResponse;
import org.meta.happiness.webide.dto.repo.*;
import org.meta.happiness.webide.dto.response.MultipleResult;
import org.meta.happiness.webide.dto.response.Result;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.security.UserDetailsImpl;
import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.repo.RepoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


// TODO: User 권한 검사 필요합니다.
@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Repository", description = "레포지토리 관련 API")
public class ReposController {

    private final RepoService repoService;
    private final ResponseService responseService;

    @PostMapping
    @Operation(summary = "신규 레포지토리 생성", description = "")
    public SingleResult<?> createRepository(
            @RequestBody RepoCreateRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        return responseService.handleSingleResult(repoService.createRepository(request, user.getUsername()));
    }

    //TODO: 지금은 creator만 조회가 가능하다.
    @GetMapping("/{repoId}")
    @Operation(summary = "개별 레포지토리 조회", description = "")
    public SingleResult<RepoResponseDto> getRepository(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable String repoId
    ) {
        return responseService.handleSingleResult(repoService.findRepo(repoId, user.getUsername()));
    }

    @GetMapping("/{userId}/recent")
    @Operation(summary = "사용자가 최근 사용한 레포지토리 조회(최대 2개)", description = "")
    public ApiResponse<?> getRecentRepository(
//            @PathVariable String projectId
    ) {
        return ApiResponse.ok();
    }

    @GetMapping("/{repoId}/files")
    @Operation(summary = "레포지토리 전체 파일 가지고 오기", description = "")
    public SingleResult<?> getRepository(
            @PathVariable("repoId") String repoId
    ) {

        return responseService.handleSingleResult(repoService.getAllFilesFromRepo(repoId));
    }


    @PatchMapping("/{repoId}")
    @Operation(summary = "레포지토리 이름 변경", description = "")
    public SingleResult<RepoResponseDto> updateRepositoryName(
            @PathVariable("repoId") String repoId,
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody RepoUpdateNameRequestDto request
    ) {
        return responseService.handleSingleResult(repoService.updateRepositoryName(repoId, request, user.getUsername()));
    }

    @DeleteMapping("/{repoId}")
    @Operation(summary = "레포지토리 삭제", description = "")
    public Result deleteRepository(
            @PathVariable("repoId") String repoId,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        repoService.deleteRepository(repoId, user.getUsername());
        return responseService.handleSuccessResult();

    }


    @GetMapping("/all")
    @Operation(summary = "사용자 전체 레포지토리 조회", description = "")
    public MultipleResult<RepoResponseDto> getAllRepositoryByUser(
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        return responseService.handleListResult(repoService.findAllRepoByUser(user.getUsername()));
    }


    @GetMapping("/{repoId}/invite")
    @Operation(summary = "나의 레포지토리 초대 링크 & 비밀번호 안내", description = "")
    public SingleResult<?> getInviteByMyRepo(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable String repoId
    ){
        return responseService.handleSingleResult(repoService.findRepoInviteInfo(repoId, user.getUsername()));
    }

    @PostMapping("/invite/{repoId}")
    @Operation(summary = "개인 레포지토리 초대", description = "")
    public Result inviteRepository(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody RepoInviteRequestDto repoInvitePassword,
            @PathVariable String repoId
    ){
        repoService.invite(repoInvitePassword.getPassword(), repoId, user.getUsername());
        return responseService.handleSuccessResult();
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
