package org.meta.happiness.webide.advice;

import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.response.Result;
import org.meta.happiness.webide.exception.*;
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
        return responseService.handleFailResult(400, "대문자 1개와 특수문자 1개를 포함해야 합니다.");
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result reLoginPlz(){
        return responseService.handleFailResult(400, "다시 로그인 하셔야 합니다.");
    }
}
