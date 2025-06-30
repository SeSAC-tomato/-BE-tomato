package com.exam.tomatoback.mypage.service;

import com.exam.tomatoback.web.dto.mypage.*;

public interface MyUserService {
    UserResponse getUserById(Long id);

    UserUpdatedResponse updateUserById(Long userId, UserUpdateRequest userUpdateRequest);

    PasswordUpdatedResponse updatePasswordById(Long userId, PasswordUpdateRequest request);
}
