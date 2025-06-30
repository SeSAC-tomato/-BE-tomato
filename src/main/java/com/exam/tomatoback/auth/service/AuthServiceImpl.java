package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.repository.UserRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public void register(RegisterRequest registerRequest) {
        boolean present = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
        // 사용자가 없을 경우
        if (present) {
            throw new TomatoException(TomatoExceptionCode.CONFLICT_USER);
        }
    }
}
