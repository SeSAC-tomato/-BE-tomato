package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.RefreshToken;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.infrastructure.util.JwtUtil;
import com.exam.tomatoback.user.model.Address;
import com.exam.tomatoback.user.model.Provider;
import com.exam.tomatoback.user.model.Role;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.auth.request.EmailCheckRequest;
import com.exam.tomatoback.web.dto.auth.request.LoginRequest;
import com.exam.tomatoback.web.dto.auth.request.NicknameCheckRequest;
import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;
import com.exam.tomatoback.web.dto.auth.response.EmailCheckResponse;
import com.exam.tomatoback.web.dto.auth.response.NicknameCheckResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void register(RegisterRequest registerRequest) {
        // 이메일 중복 여부 확인
        if(userService.existsByEmail(registerRequest.getEmail())) {
            throw new TomatoException(TomatoExceptionCode.DUPLICATE_USER);
        }
        // 닉네임 중복 여부 확인
        if(userService.existsByNickname(registerRequest.getNickname())) {
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
        userService.save(newUser);
    }

    @Override
    public void login(LoginRequest loginRequest, HttpServletResponse response) {
        // 사용자 정보 조회
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        // 인증에 성공을 할 경우 정보를 저장
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        String accessToken = jwtUtil.getAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);
        // 생성된 토큰을 반환
        response.setHeader(Constants.AUTH_HEADER, accessToken);
        response.addCookie(createCookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken()));
    }

    @Override
    public EmailCheckResponse emailCheck(EmailCheckRequest request) {
        if (userService.existsByEmail(request.email())) {
            return EmailCheckResponse.unavailable();
        }
        return EmailCheckResponse.available();
    }

    @Override
    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest request) {
        if(userService.existsByNickname(request.nickname())) {
            return NicknameCheckResponse.unavailable();
        };
        return NicknameCheckResponse.available();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userService.getOptionalUser(username).orElseThrow(
                () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
            );
        } else {
            throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
        }
    }

    @Override
    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails) {
            return userDetails;
        } else {
            throw new TomatoException(TomatoExceptionCode.UNABLE_AUTH_INFO);
        }
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshTokenOpt = getRefreshTokenByCookie(request);
        if(refreshTokenOpt.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String refreshTokenString = refreshTokenOpt.get();

        UserDetails userDetails = getCurrentUserDetails();

        if(!jwtUtil.validate(refreshTokenString, userDetails)) {
            throw new TomatoException(TomatoExceptionCode.INVALID_REFRESH_TOKEN);
        }
        // 재발급 토큰이 DB에 없을 경우
        if(refreshTokenService.isInvalid(refreshTokenString)) {
            throw new TomatoException(TomatoExceptionCode.INVALID_REFRESH_TOKEN);
        }

        // 재발급 토큰이 유효할 경우
        // 접근 토큰을 재발급 후 헤더에 담아서 반환
        String accessToken = jwtUtil.getAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);
        // 생성된 토큰을 반환
        response.setHeader(Constants.AUTH_HEADER, accessToken);
        response.addCookie(createCookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken()));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        // 쿠키 유효시간 7일
        cookie.setMaxAge((int) Duration.ofDays(7L).toSeconds());
        // js 접근 차단
        cookie.setHttpOnly(true);
        // 전체 경로에서 접근 -> 프론트 주소가 생길 경우 수정
        cookie.setPath("/");
        // 로컬 환경에서 진행하기 때문에 false 로 진행 추후 통신을 https 로 할 경우 true 로 변경
        cookie.setSecure(false);

        return cookie;
    }

    private Optional<String> getRefreshTokenByCookie(HttpServletRequest request) {
        if(request.getCookies() == null) return Optional.empty();

        for (Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(Constants.REFRESH_TOKEN_COOKIE_NAME)) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
