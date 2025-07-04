package com.exam.tomatoback.web.dto.auth.request;

import com.exam.tomatoback.auth.model.VerityType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VerifyRequest(
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    String email,
    @NotNull(message = "인증 토큰은 필수입니다.")
    String token,
    @NotNull(message = "인증 타입은 필수입니다.")
    VerityType type
) {
}
