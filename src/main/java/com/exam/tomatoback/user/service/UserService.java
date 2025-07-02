package com.exam.tomatoback.user.service;

import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.web.dto.mypage.request.PasswordUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.request.UserUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.response.PasswordUpdatedResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserResponse;
import com.exam.tomatoback.web.dto.mypage.response.UserUpdatedResponse;

import java.util.Optional;

public interface UserService {
    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> getOptionalUser(String email);

    // userId 로 사용자 정보 조회
    UserResponse getUserById(Long id);

    // userId 로 사용자 정보 수정
    UserUpdatedResponse updateUserById(Long userId, UserUpdateRequest request);

    // userId로 비밀번호 수정
    PasswordUpdatedResponse updatePasswordById(Long userId, PasswordUpdateRequest request);
}
