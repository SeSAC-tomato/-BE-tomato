package com.exam.tomatoback.web.dto.auth.response;

import lombok.Builder;

@Builder
public record NicknameCheckResponse(
    boolean duplication,
    String message
) {
  public static NicknameCheckResponse success() {
    return NicknameCheckResponse.builder()
        .duplication(true)
        .message("이미 사용중인 닉네임 입니다.")
        .build();
  }

  public static NicknameCheckResponse fail() {
    return NicknameCheckResponse.builder()
        .duplication(false)
        .message("사용 가능한 닉네임 입니다.")
        .build();
  }
}
