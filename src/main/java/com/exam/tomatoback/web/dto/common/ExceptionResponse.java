package com.exam.tomatoback.web.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(
    int status,
    String code,
    String message,
    Map<String, String> errors
) {
 public static ExceptionResponse of(HttpStatus status, String code, String message) {
  return ExceptionResponse.builder()
      .status(status.value())
      .code(code)
      .message(message)
      .build();
 }
 public static ExceptionResponse of(HttpStatus status, String code, String message, Map<String, String> errors) {
  return ExceptionResponse.builder()
          .status(status.value())
          .code(code)
          .message(message)
          .errors(errors)
          .build();
 }
}
