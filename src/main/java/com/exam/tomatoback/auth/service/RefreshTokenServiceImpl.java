package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.RefreshToken;
import com.exam.tomatoback.auth.repository.RefreshTokenRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.JwtUtil;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(UserDetails userDetails) {
        // 기존에 refresh 토큰이 존재 하는지 여부를 확인할 필요가 있음
        User user = userService.getOptionalUser(userDetails.getUsername()).orElseThrow(
            (() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND))
        );
        // 사용자의 id 를 통해 RefreshToken 이 있을 경우 해당 토큰을 삭제
        refreshTokenRepository.findByUser_Id(user.getId()).ifPresent(refreshTokenRepository::delete);
        // 토큰 생성 후 저장
        return refreshTokenRepository.save(RefreshToken.builder()
            .user(user)
            .token(jwtUtil.getRefreshToken(userDetails))
            .build()
        );
    }

    @Override
    public Boolean isInvalid(String refreshToken) {
        return !refreshTokenRepository.existsByToken(refreshToken);
    }

    @Override
    public void deleteToken(User currentUser) {
        refreshTokenRepository.findByUser_Id(currentUser.getId()).ifPresent(refreshTokenRepository::delete);
    }
}
