package com.exam.tomatoback.web.dto.common;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResponse(
    HttpStatus status,
    String code,
    String message
) {}
