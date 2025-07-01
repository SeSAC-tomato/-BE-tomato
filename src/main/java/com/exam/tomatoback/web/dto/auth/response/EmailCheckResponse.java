package com.exam.tomatoback.web.dto.auth.response;

import lombok.Builder;

/**
 * 이메일 중복 여부 결과 반환 dto
 * @param duplication 중복 여부
 * @param message 메세지
 */
@Builder
public record EmailCheckResponse(
    boolean duplication,
    String message
) {
  public static EmailCheckResponse unavailable() {
    return EmailCheckResponse.builder()
        .duplication(true)
        .message("이미 사용중인 이메일 입니다.")
        .build();
  }

  public static EmailCheckResponse available() {
    return EmailCheckResponse.builder()
        .duplication(false)
        .message("사용 가능한 이메일입니다.")
        .build();
  }
}
