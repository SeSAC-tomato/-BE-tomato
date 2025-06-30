package com.exam.tomatoback.mypage.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.mypage.repository.MyUserRepository;
import com.exam.tomatoback.web.dto.mypage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.exam.tomatoback.user.model.*;

import static com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MyUserServiceImpl implements MyUserService {

    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new TomatoException(USER_NOT_FOUND_IN_MYPAGE));

        String address = null;
        if(user.getAddress() != null) {
            address = user.getAddress().getAddress();
        }
        return new UserResponse(
                user.getNickname(),
                user.getEmail(),
                address
        );
    }

    @Transactional
    @Override
    public UserUpdatedResponse updateUserById(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new TomatoException(USER_NOT_FOUND_IN_MYPAGE));
        user.setNickname(request.getNickname());
        Address address = user.getAddress();
        if(address != null) {
            address.setAddress(request.getAddress());
        } else{
            Address newAddress = Address.builder()
                    .user(user)
                    .address(request.getAddress())
                    .build();
            user.setAddress(newAddress);
        }

        return new UserUpdatedResponse(user.getNickname(), user.getAddress() != null ? user.getAddress().getAddress() : null);
    }

    @Transactional
    @Override
    public PasswordUpdatedResponse updatePasswordById(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new TomatoException(USER_NOT_FOUND_IN_MYPAGE));

        // 1. 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new TomatoException(PASSWORD_INCORRECT_IN_MYPAGE);
        }

        // 2. 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new TomatoException(PASSWORD_MISMATCH_IN_MYPAGE);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return PasswordUpdatedResponse.builder()
                .message("비밀번호가 성공적으로 변경되었습니다.")
                .build();
    }
}
