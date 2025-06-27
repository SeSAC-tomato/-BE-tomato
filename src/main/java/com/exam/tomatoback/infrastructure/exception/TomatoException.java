package com.exam.tomatoback.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TomatoException extends RuntimeException{
 private final HttpStatus status;
 private final String code;
 private final String message;

 public TomatoException(TomatoExceptionCode code) {
  this.code = code.getCode();
  this.status = code.getStatus();
  this.message = code.getMessage();
 }
}
