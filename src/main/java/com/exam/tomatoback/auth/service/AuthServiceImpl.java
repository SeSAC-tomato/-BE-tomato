package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.auth.model.RefreshToken;
import com.exam.tomatoback.auth.model.UserVerify;
import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.infrastructure.util.GeometryUtil;
import com.exam.tomatoback.infrastructure.util.JwtUtil;
import com.exam.tomatoback.user.model.Address;
import com.exam.tomatoback.user.model.Provider;
import com.exam.tomatoback.user.model.Role;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.auth.request.*;
import com.exam.tomatoback.web.dto.auth.response.EmailCheckResponse;
import com.exam.tomatoback.web.dto.auth.response.KakaoAddressResponse;
import com.exam.tomatoback.web.dto.auth.response.NicknameCheckResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final MailService mailService;
    private final UserVerifyService userVerifyService;
    private final KakaoLocalService kakaoLocalService;

    @Override
    public void register(RegisterRequest registerRequest) {
        registerVerify(registerRequest);
        // 비밀번호 암호화
        registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));

        // 신규 사용자 정보 설정
        User newUser = User.builder()
            .email(registerRequest.getEmail())
            .nickname(registerRequest.getNickname())
            .password(registerRequest.getPassword())
            .provider(Provider.LOCAL)
            .role(Role.USER)
            .verify(false)
            .build();

        // 주소 정보 설정
        Address address = Address.builder()
            .user(newUser)
            .address(registerRequest.getAddress())
            .sido(registerRequest.getSido())
            .sigungu(registerRequest.getSigungu())
            .dong(registerRequest.getDong())
            .build();

        newUser.setAddress(address);

        // 사용자 저장
        userService.save(newUser);

        // 이메일 전송
        mailService.sendEmailVerify(newUser.getEmail(), newUser.getNickname());
    }

    private void registerVerify(RegisterRequest registerRequest) {
        // 이메일 중복 여부 확인
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new TomatoException(TomatoExceptionCode.DUPLICATE_USER);
        }
        // 닉네임 중복 여부 확인
        if (userService.existsByNickname(registerRequest.getNickname())) {
            throw new TomatoException(TomatoExceptionCode.DUPLICATE_USER);
        }
        // 비밀번호 검증
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            throw new TomatoException(TomatoExceptionCode.PASSWORD_MISMATCH);
        }
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
        response.addCookie(createCookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken(), 7L));
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
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshTokenOpt = getRefreshTokenByCookie(request);
        if(refreshTokenOpt.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String refreshTokenString = refreshTokenOpt.get();

        if(jwtUtil.isExpired(refreshTokenString)) {
            throw new TomatoException(TomatoExceptionCode.TOKEN_EXPIRED);
        }
        // 재발급 토큰이 DB에 없을 경우
        if(refreshTokenService.isInvalid(refreshTokenString)) {
            throw new TomatoException(TomatoExceptionCode.INVALID_REFRESH_TOKEN);
        }

        User userByRefreshToken = refreshTokenService.getUserByRefreshToken(refreshTokenString);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userByRefreshToken.getEmail());

        // 재발급 토큰이 유효할 경우
        // 접근 토큰을 재발급 후 헤더에 담아서 반환
        String accessToken = jwtUtil.getAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);
        // 생성된 토큰을 반환
        response.setHeader(Constants.AUTH_HEADER, accessToken);
        response.addCookie(createCookie(Constants.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken(), 7L));
    }

    @Override
    public void logout(HttpServletResponse response) {
        // 1. 쿠키에 저장된 refresh_token 제거
        response.addCookie(createCookie(Constants.REFRESH_TOKEN_COOKIE_NAME, "", 0L));

        // 2. 서버 DB에 저장된 refresh token 삭제
        try {
            User user = userService.getCurrentUser();
            refreshTokenService.deleteToken(user);
        } catch (TomatoException e) {
            // 유효하지 않은 사용자일 수 있으니 무시 하고 로그
            log.info("로그아웃 요청 시 인증 정보 없음: {}", e.getMessage());
        }

        // 3. SecurityContext 삭제 (메모리 상의 인증 정보 제거)
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional
    public void verify(VerifyRequest request) {
        // 인증할 사용자의 정보 조회
        User user = userService.getOptionalUser(request.email()).orElseThrow(
            () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
        );
        // 인증 타입이 비밀번호가 아닌 경우에 아래 코드 실행
        // 이미 인증된 사용자의 경우 이미 인증이 되었다고 로그인 하라고 반환
        if (!request.type().equals(VerityType.PASSWORD) && user.getVerify()) {
            throw new TomatoException(TomatoExceptionCode.ALREADY_USER);
        }
        // 넘어온 토큰과 해당 사용자의 일치 여부와 타입 일치 여부 확인
        UserVerify verify = userVerifyService.verify(request, true);

        // 비밀번호 인증의 경우 토큰을 사용자가 비밀번호 수정 했을 때 삭제를 하도록 하게 해야함
        // 이메일 인증의 경우
        // 인증된 사용자로 수정을 해주고
        // 인증 토큰 삭제
        if (request.type().equals(VerityType.EMAIL)) {
            user.setVerify(true);
            // 사용자의 주소를 좌표로 변환 하고 이를 db 에 저장
            KakaoAddressResponse kakaoAddressResponse = kakaoLocalService.searchAddress(user.getAddress().getAddress());
            Point point = GeometryUtil.exchangePoint(kakaoAddressResponse.getDocuments().get(0).getX(), kakaoAddressResponse.getDocuments().get(0).getY());
            user.getAddress().setPoint(point);
            userVerifyService.delete(verify);
        }
    }

    @Override
    public void reverify(VerifyRequest request) {
        // 토큰 만료를 제외한 인증 정보 검증
        userVerifyService.verify(request, false);

        // 사용자 조회
        User user = userService.getOptionalUser(request.email()).orElseThrow(
            () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
        );
        // 인증 메일 재전송
        mailService.sendEmailVerify(user.getEmail(), user.getNickname());
    }

    @Override
    public void findPassword(FindPasswordRequest request) {
        User user = userService.getOptionalUser(request.email()).orElseThrow(
            () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
        );

        mailService.sendPasswordVerify(user.getEmail(), user.getNickname());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        // 사용자가 있는지 여부 확인
        User user = userService.getOptionalUser(request.email()).orElseThrow(
            () -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND)
        );
        // 인증 정보를 확인하기 위한 dto 로 면화
        VerifyRequest verifyRequest = VerifyRequest.builder()
            .email(request.email())
            .token(request.token())
            .type(request.type())
            .build();
        // 위 dto 를 통한 인증 정보 검증
        UserVerify verify = userVerifyService.verify(verifyRequest, true);
        // 직전 사용한 비밀번호의 경우 409 에러가 발생하도록 하게 함
        if (encoder.matches(request.password(), user.getPassword())) {
            throw new TomatoException(TomatoExceptionCode.PASSWORD_DUPLICATED);
        }
        // 검증까지 통과한 경우 비밀번호를 암호화 하여 새로 지정
        user.setPassword(encoder.encode(request.getPassword()));

        // 새로 지정 후 인증 정보 삭제
        userVerifyService.delete(verify);
    }

    private Cookie createCookie(String key, String value, Long days) {
        Cookie cookie = new Cookie(key, value);
        // 쿠키 유효시간 7일
        cookie.setMaxAge((int) Duration.ofDays(days).toSeconds());
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
