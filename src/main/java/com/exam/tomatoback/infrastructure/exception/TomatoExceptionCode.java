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
 );

 private HttpStatus status;
 private String code;
 private String message;
}
