package com.exam.tomatoback.infrastructure.exception;

import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.common.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
 // Tomato 커스텀 예외 헨들러
 @ExceptionHandler(TomatoException.class)
 public ResponseEntity<?> tomatoExceptionHandler(TomatoException e) {
  log.error(e.getMessage());
  return ResponseEntity.status(e.getStatus()).body(CommonResponse.fail(ExceptionResponse.of(e.getStatus(),e.getCode(), e.getMessage())));
 }

 // Validation 예외 헨들러 (@Valid 실패)
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<?> tomatoExceptionHandler(MethodArgumentNotValidException e) {
  log.warn("Validation 실패: {}", e.getMessage());

  Map<String, String> errors = new HashMap<>();

  for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
   errors.put(fieldError.getField(), fieldError.getDefaultMessage());
  }

  ExceptionResponse response = ExceptionResponse.of(
          HttpStatus.BAD_REQUEST,
          "VALIDATION_ERROR",
          "입력값이 유효하지 않습니다.",
          errors // 필드별 메시지 포함
  );

  return ResponseEntity
          .badRequest()
          .body(CommonResponse.fail(response));
 }

 // JWT 만료 예외 헨들러
 @ExceptionHandler(ExpiredJwtException.class)
 public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException e) {
  log.warn("JWT 토큰 만료: {}", e.getMessage());

  ExceptionResponse response = ExceptionResponse.of(
      HttpStatus.UNAUTHORIZED,
      "JWT_EXPIRED",
      "인증 토큰이 만료되었습니다. 다시 로그인해주세요."
  );

  return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(CommonResponse.fail(response));
 }

}
