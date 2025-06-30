package com.exam.tomatoback.infrastructure.util;

public final class Constants {
    private Constants() {}

    // ====== 인증 관련 경로 ======
    public static final String[] PUBLIC_PATH = {"/api/v1/auth/**"};

    public static final String[] ADMIN_PATH = {};

    // ====== 토큰 관련 ======
    public static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
}
