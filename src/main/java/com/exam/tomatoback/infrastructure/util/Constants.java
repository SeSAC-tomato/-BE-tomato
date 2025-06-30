package com.exam.tomatoback.infrastructure.util;

public final class Constants {
    private Constants() {}

    // ====== 인증 관련 경로 ======
    public static final String[] PUBLIC_PATH = {"/api/v1/auth/**", "/api/v1/user/**"};

    public static final String[] ADMIN_PATH = {};

    // ====== 토큰 관련 ======
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
}
