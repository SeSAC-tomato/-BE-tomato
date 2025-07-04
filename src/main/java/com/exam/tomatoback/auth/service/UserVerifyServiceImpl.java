package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.UserVerify;
import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.auth.repository.UserVerifyRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserVerifyServiceImpl implements UserVerifyService {
  private final UserVerifyRepository userVerifyRepository;
  private final UserService userService;

  @Override
  public void save(String email, String token, VerityType type) {
    User user = userService.getOptionalUser(email).orElseThrow(
        () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
    );

    userVerifyRepository.save(UserVerify.builder()
        .user(user)
        .token(token)
        .verityType(type)
        // 1시간의 유효시간
        .expiresAt(Instant.now().plusSeconds(3600))
        .build());
  }
}
