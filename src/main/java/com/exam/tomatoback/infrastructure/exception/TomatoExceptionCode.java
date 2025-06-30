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
 ), USER_NOT_FOUND_IN_MYPAGE(
         HttpStatus.NOT_FOUND,
         "TOMATO_MYPAGE_001",
         "사용자 정보를 조회하지 못 했습니다."
 ), PASSWORD_MISMATCH_IN_MYPAGE(
         HttpStatus.BAD_REQUEST,
         "TOMATO_MYPAGE_002",
         "변경할 비밀번호가 확인비밀번호와 일치하지 않습니다."
 ), PASSWORD_INCORRECT_IN_MYPAGE(
         HttpStatus.BAD_REQUEST,
         "TOMATO_MYPAGE_003",
         "비밀번호가 일치하지 않습니다."
 );

 private HttpStatus status;
 private String code;
 private String message;
}
