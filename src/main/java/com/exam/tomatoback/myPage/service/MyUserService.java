package com.exam.tomatoback.mypage.service;

import com.exam.tomatoback.mypage.repository.MyUserRepository;
import com.exam.tomatoback.web.dto.myPageDto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MyUserService {

    private final MyUserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NoSuchElementException("유저가 존재하지 않습니다."));

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
}
