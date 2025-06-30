package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.Address;
import com.exam.tomatoback.user.model.Provider;
import com.exam.tomatoback.user.model.Role;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void register(RegisterRequest registerRequest) {
        // 이메일 중복 여부 확인
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new TomatoException(TomatoExceptionCode.DUPLICATE_USER);
        }
        // 닉네임 중복 여부 확인
        if(userRepository.existsByNickname(registerRequest.getNickname())) {
            throw new TomatoException(TomatoExceptionCode.DUPLICATE_USER);
        }
        // 비밀번호 검증
        if(!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            throw new TomatoException(TomatoExceptionCode.PASSWORD_MISMATCH);
        }
        // 비밀번호 암호화
        registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));

        // 신규 사용자 정보 설정
        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .nickname(registerRequest.getNickname())
                .password(registerRequest.getPassword())
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .build();

        // 주소 정보 설정
        Address address = Address.builder()
                .user(newUser)
                .address(registerRequest.getAddress())
                .build();

        newUser.setAddress(address);

        // 사용자 저장
        userRepository.save(newUser);
    }
}
