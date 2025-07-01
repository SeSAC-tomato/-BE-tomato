package com.exam.tomatoback.web.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameCheckRequest(
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이어야 합니다.")
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    String nickname
) {
}
