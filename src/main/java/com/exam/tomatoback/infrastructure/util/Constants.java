package com.exam.tomatoback.infrastructure.util;

public final class Constants {
    private Constants() {}

    // ====== 인증 관련 경로 ======
    public static final String[] PUBLIC_PATH = {
            "/api/v1/auth/register",    // 회원가입
            "/api/v1/auth/login",       // 로그인
            "/api/v1/auth/email",       // 이메일 중복 확인
            "/api/v1/auth/nickname",    // 닉네임 중복 확인
            "/api/v1/auth/refresh",     // 토큰 재발행 해당 기능의 경우 accesstoken 이 없어도 가능하게 해야함
            "/websocket/**",            // 웹소켓 핸드쉐이크
            "/api/v1/tes/**"            // 테스트용

    };

    public static final String[] ADMIN_PATH = {};

    // ====== 토큰 관련 ======
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";


    // 챗 이미지 저장
    public static final String CHAT_IMAGE_DIR = "./uploads/chat";
}
