package org.meta.happiness.webide.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.user.*;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.common.exception.ExistAccountException;
import org.meta.happiness.webide.common.exception.LoginFailureException;
import org.meta.happiness.webide.common.exception.RefreshTokenException;
import org.meta.happiness.webide.common.exception.UserNicknameDuplicatedException;
import org.meta.happiness.webide.common.exception.UserNotFoundException;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.common.security.jwt.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Transactional
    public UserResponseDto
    registerUser(UserRegisterDto form) {
        validateDuplicatedUser(form.getEmail(), form.getNickname());
        User user = User.createUser(form, passwordEncoder.encode(form.getPassword()),null);
        User savedUser = userRepository.save(user);
        log.info("user={}", savedUser);

        return new UserResponseDto(savedUser.getId(),savedUser.getNickname(),
                savedUser.getEmail(),savedUser.getCreatedDate(),savedUser.getLastModifiedDate());
    }
    /* @Transactional
     public UserDto registerMaster(UserRegisterDto form) {
         validateDuplicatedUser(form.getEmail(), form.getNickname());
         Role role = Role.ADMIN;
         User user = User.createUser(form, passwordEncoder.encode(form.getPassword()), role);
         User savedUser = userRepository.save(user);
         return new UserDto(savedUser.getId(),savedUser.getNickname(),
                 savedUser.getEmail(),savedUser.getCreatedDate(),savedUser.getLastModifiedDate());
     }*/
    @Transactional
    public UserLoginResponseDto loginUser(LoginFormDto form) {
        User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(form.getPassword(), user.getPassword())) {
            throw new LoginFailureException();
        }
        return new UserLoginResponseDto(user.getId(), user.getEmail(), user.getNickname(),
                jwtUtil.generateToken(user.getEmail()) /*jwtUtil.refreshToken(user.getEmail())*/
        );
    }

    private void validateDuplicatedUser(String email, String nickname) {
        if(userRepository.findByEmail(email).isPresent()) throw new ExistAccountException();
        if(userRepository.findByNickname(nickname).isPresent()) throw new UserNicknameDuplicatedException();
    }

    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll()
                .stream().map(UserResponseDto::convertUserToDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto findUserEmail(String userEmail) {
        return UserResponseDto.convertUserToDto(userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new));
    }

    public UserResponseDto findUser(Long userId) {
        return UserResponseDto.convertUserToDto(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }
    //유저 정보 변경
    @Transactional
    public UserResponseDto updateUser(String userEmail, UserUpdateDto request) {
        User findUser = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);
        validateDuplicatedNickname(request.getNickname());
        findUser.changeNickname(request.getNickname());
        findUser.changePassword(passwordEncoder.encode(request.getPassword()));
        return UserResponseDto.convertUserToDto(findUser);
    }
    //유저 삭제
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
    //중복 닉네임 확인
    private void validateDuplicatedNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new UserNicknameDuplicatedException();
        }
    }
    public UserLoginResponseDto refreshToken(String token) {
        User user = userRepository.findByEmail(jwtUtil.getEmailFromToken(token)).orElseThrow(UserNotFoundException::new);
        //구현해야할 것 토큰이 만료되기 전에만 refresh가능하도록 만료되면 재로그인 과정을 거쳐야 함.
        boolean checkValid = jwtUtil.isTokenValid(token);
        if (checkValid) {
            user.changeRefreshToken(jwtUtil.generateRefreshToken(user.getEmail()));
            return new UserLoginResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getRefreshToken());
        }else{
            throw new RefreshTokenException();
        }
    }

}
