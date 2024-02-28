package org.meta.happiness.webide.common.advice;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.common.exception.*;
import org.meta.happiness.webide.dto.response.Result;;
import org.meta.happiness.webide.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "org.meta.happiness.webide.controller")
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result defaultException() {
        return responseService.handleFailResult(500, "오류가 발생 하였습니다.");
    }

    @ExceptionHandler(ExistAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result userAccountAlreadyExist() {
        return responseService.handleFailResult(409, "입력하신 이메일이 이미 존재합니다.");
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result loginFail() {
        return responseService.handleFailResult(400, "이메일 또는 비밀번호를 잘못 입력하였습니다.");
    }

    @ExceptionHandler(UserNicknameDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result nickIsDuplicated() {
        return responseService.handleFailResult(404, "입력하신 닉네임이 이미 존재합니다.");
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userNotfound() {
        return responseService.handleFailResult(500, "유저를 찾을 수 없습니다.");
    }
    @ExceptionHandler(EmailPatternException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result failInputEmail() {
        return responseService.handleFailResult(400, "email형식의 회원가입만 가능합니다.");
    }

    @ExceptionHandler(PasswordPatternException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result failInputPassword() {
        return responseService.handleFailResult(400, "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8 ~ 20자의 비밀번호여야 합니다.");
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result reLoginPlz(){
        return responseService.handleFailResult(400, "다시 로그인 하셔야 합니다.");
    }


    @ExceptionHandler(FileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result fileAlreadyExist(){
        return responseService.handleFailResult(400, "해당 파일이 이미 존재합니다");
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result fileNotFound(){
        return responseService.handleFailResult(400, "해당 파일이 존재하지 않습니다");
    }

    @ExceptionHandler(FileMetaDataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result fileMetadataNotFound(){
        return responseService.handleFailResult(400, "파일 메타데이터 정보가 존재하지 않습니다.");
    }

    @ExceptionHandler(RepoNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result repoNotFoundEx(){
        return responseService.handleFailResult(400, "레포지토리가 존재하지 않음");}

    @ExceptionHandler(FileMetaDataPathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result fileMetadataPathErr(){
        return responseService.handleFailResult(500, "파일 메타데이터 경로 예외가 발생했습니다");}

    @ExceptionHandler(RepositoryCreatorMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result repoNotCreator(){
        return responseService.handleFailResult(400, "삭제 권한이 없습니다. [생성자 아님]");
    }

    @ExceptionHandler(S3UploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result s3UploadErr(){
        return responseService.handleFailResult(500, "S3 업로드에 문제가 발생하였습니다.");
    }
}
