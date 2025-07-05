package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.web.dto.auth.request.*;
import com.exam.tomatoback.web.dto.auth.response.EmailCheckResponse;
import com.exam.tomatoback.web.dto.auth.response.NicknameCheckResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    /**
     * 신규 사용자 등록 시 사용할 메서드 입니다.
     * 반환 값은 없습니다.
     * @param registerRequest 회원 가입 정보
     */
    void register(RegisterRequest registerRequest);

    /**
     * LoginRequest를 사용해 사용자의 로그인 시 필요한 정보를 처리하며 HttpServletResponse 를 사용해 토큰 정보를 사용자에게 반환하게 됩니다.
     *
     * @param loginRequest 로그인 이메일 및 비밀번호
     * @param response     토큰 정보를 넘기기 위해 필요한 변수
     */
    void login(LoginRequest loginRequest, HttpServletResponse response);

    EmailCheckResponse emailCheck(EmailCheckRequest request);

    NicknameCheckResponse nicknameCheck(NicknameCheckRequest request);

    void refresh(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletResponse request);

    void verify(VerifyRequest request);

    void reverify(VerifyRequest request);

    void findPassword(FindPasswordRequest request);
}
