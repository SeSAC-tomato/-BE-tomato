package com.exam.tomatoback.infrastructure.exception;

import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.common.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(TomatoException.class)
 public ResponseEntity<?> tomatoExceptionHandler(TomatoException e) {
  log.error(e.getMessage());
  return ResponseEntity.status(e.getStatus()).body(CommonResponse.fail(ExceptionResponse.setMessage(e.getStatus(),e.getCode(), e.getMessage())));
 }
}
