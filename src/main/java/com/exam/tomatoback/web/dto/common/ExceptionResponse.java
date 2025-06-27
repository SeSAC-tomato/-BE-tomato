package com.exam.tomatoback.web.dto.common;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(
    int status,
    String code,
    String message
) {
 public static ExceptionResponse setMessage(HttpStatus status, String code, String message) {
  return ExceptionResponse.builder()
      .status(status.value())
      .code(code)
      .message(message)
      .build();
 }
}
