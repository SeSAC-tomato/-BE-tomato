package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.UserVerify;
import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.auth.repository.UserVerifyRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.auth.request.VerifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 만약에 이미 인증 정보가 있을 경우 삭제
    userVerifyRepository.findByUser(user).ifPresent((verify) -> {
      // 인증 타입이 일치할 때만 삭제
      if (verify.getVerityType().equals(type)) {
        userVerifyRepository.delete(verify);
      }
    });

    userVerifyRepository.save(UserVerify.builder()
        .user(user)
        .token(token)
        .verityType(type)
        // 1시간의 유효시간
        .expiresAt(Instant.now().plusSeconds(3600))
        .build());
  }

  @Override
  public UserVerify verify(VerifyRequest request, Boolean expiredCheck) {
    UserVerify userVerify = userVerifyRepository.findByToken(request.token()).orElseThrow(
        () -> new TomatoException(TomatoExceptionCode.INVALID_VERIFY_TOKEN)
    );
    if (!userVerify.getUser().getEmail().equalsIgnoreCase(request.email())) {
      throw new TomatoException(TomatoExceptionCode.INVALID_VERIFY_USER);
    }
    if(expiredCheck && userVerify.getExpiresAt().isBefore(Instant.now())) {
      throw new TomatoException(TomatoExceptionCode.VERIFY_TOKEN_EXPIRED);
    }

    switch (request.type()) {
      case EMAIL -> {
        if (!userVerify.getVerityType().equals(VerityType.EMAIL)) {
          throw new TomatoException(TomatoExceptionCode.VERIFY_NOT_EQUALS_TYPE);
        }
      }
      case PASSWORD -> {
        if (!userVerify.getVerityType().equals(VerityType.PASSWORD)) {
          throw new TomatoException(TomatoExceptionCode.VERIFY_NOT_EQUALS_TYPE);
        }
      }
      default -> throw new TomatoException(TomatoExceptionCode.VERIFY_NOT_EQUALS_TYPE);
    }

    return userVerify;
  }

  @Override
  @Transactional
  public void delete(UserVerify verify) {
    userVerifyRepository.delete(verify);
  }
}
