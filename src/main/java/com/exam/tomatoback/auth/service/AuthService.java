package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;

public interface AuthService {

    /**
     * 신규 사용자 등록 시 사용할 메서드 입니다.
     * 반환 값은 없습니다.
     * @param registerRequest 회원 가입 정보
     */
    void register(RegisterRequest registerRequest);
}
