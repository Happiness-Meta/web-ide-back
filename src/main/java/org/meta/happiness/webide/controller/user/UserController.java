package org.meta.happiness.webide.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.response.MultipleResult;
import org.meta.happiness.webide.dto.response.SingleResult;
import org.meta.happiness.webide.dto.user.*;
import org.meta.happiness.webide.exception.EmailPatternException;
import org.meta.happiness.webide.exception.PasswordPatternException;
import org.meta.happiness.webide.security.JwtUtil;
import org.meta.happiness.webide.security.UserDetailsImpl;
import org.meta.happiness.webide.service.ResponseService;
import org.meta.happiness.webide.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.meta.happiness.webide.dto.response.Result;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final ResponseService responseService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/sign/register")
    public SingleResult<UserResponseDto> register(@RequestBody @Validated UserRegisterDto form,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("email"))  // request 단에서 넘어온 양식을 validation으로 check하고,
            //문제 있을 시 Exception을 던져줌 Exception은 ExceptionAdvice class에서 다루고 있음.
            throw new EmailPatternException();
        if(bindingResult.hasFieldErrors("password"))
            throw new PasswordPatternException();

        return responseService.handleSingleResult(userService.registerUser(form));
    }

    @GetMapping("/user")
    public SingleResult<UserResponseDto> checkUser(
            @AuthenticationPrincipal UserDetailsImpl user
            ){

        return responseService.handleSingleResult(userService.findUserEmail(user.getUsername()));
    }


    //로그인
    @PostMapping("/sign/login")
    public SingleResult<UserLoginResponseDto> login(@RequestBody LoginFormDto form) {
        return responseService.handleSingleResult(userService.loginUser(form));
    }
    //모든 회원 조회
    @GetMapping("/users")
    public MultipleResult<UserResponseDto> findAllUsers() {
        return responseService.handleListResult(userService.findAllUser());
    }
    //단일 회원 조회
    @GetMapping("/user/{id}")
    public SingleResult<UserResponseDto> findUser(@PathVariable("id")Long userId) {
        return responseService.handleSingleResult(userService.findUser(userId));
    }
    //회원 정보 수정
    @PutMapping("/user")
    public SingleResult<UserResponseDto> updateUser(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestBody UserUpdateDto request) {
        return responseService.handleSingleResult(userService.updateUser(user.getUsername(), request));
    }
    //회원 삭제
    @DeleteMapping("/user/{id}")
    public Result deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return responseService.handleSuccessResult();
    }
    //토큰 재발급
    @PostMapping("/refresh")
    public SingleResult<UserLoginResponseDto> refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String auth = headerAuth.substring(7);
        return responseService.handleSingleResult(userService.refreshToken(auth));
    }
}
