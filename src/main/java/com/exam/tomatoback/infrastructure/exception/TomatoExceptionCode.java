package com.exam.tomatoback.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TomatoExceptionCode {
 INTERNAL_SERVER_ERROR(
         HttpStatus.INTERNAL_SERVER_ERROR,
         "TOMATO_000",
         "알 수 없는 서버 에러가 발생하였습니다."
 ), USER_NOT_FOUND(
         HttpStatus.NOT_FOUND,
         "TOMATO_AUTH_001",
         "사용자 정보를 조회하지 못 했습니다."
 ), DUPLICATE_USER(
         HttpStatus.CONFLICT,
         "TOMATO_AUTH_002",
         "이미 등록된 사용자 입니다."
 ), PASSWORD_MISMATCH(
         HttpStatus.BAD_REQUEST,
         "TOMATO_AUTH_003",
         "비밀번호가 일치하지 않습니다."
 ), ASSOCIATED_USER_NOT_FOUND(
         HttpStatus.NOT_FOUND,
         "TOMATO_POST_001",
         "게시글에 연결된 사용자 정보를 조회할 수 없습니다"
 ), REFRESH_TOKEN_NOT_FOUND(
     HttpStatus.UNAUTHORIZED,
     "TOMATO_AUTH_004",
     "RefreshToken을 찾을 수 없습니다."
 ), INVALID_REFRESH_TOKEN(
     HttpStatus.UNAUTHORIZED,
     "TOMATO_AUTH_005",
     "잘못된 RefreshToken 입니다."
 ), UNABLE_AUTH_INFO(
     HttpStatus.UNAUTHORIZED,
     "TOMATO_AUTH_006",
     "인증 정보를 조회할 수 없습니다."
 ),
 EMAIL_SEND_FAILED(
     HttpStatus.INTERNAL_SERVER_ERROR,
     "TOMATO_AUTH_007",
     "이메일 전송에 실패했습니다."
 );

 private HttpStatus status;
 private String code;
 private String message;
}
