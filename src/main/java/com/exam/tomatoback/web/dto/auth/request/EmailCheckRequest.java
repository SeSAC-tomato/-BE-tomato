package com.exam.tomatoback.web.dto.auth.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailCheckRequest(
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    String email
) {
}
