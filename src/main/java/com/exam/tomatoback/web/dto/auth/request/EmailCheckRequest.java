package com.exam.tomatoback.web.dto.auth.request;


import jakarta.validation.constraints.Email;

public record EmailCheckRequest(
    @Email(message = "이메일 형식이 아닙니다.")
    String email
) {
}
