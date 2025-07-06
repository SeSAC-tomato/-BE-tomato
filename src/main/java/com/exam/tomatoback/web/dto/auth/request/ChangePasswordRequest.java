package com.exam.tomatoback.web.dto.auth.request;

import com.exam.tomatoback.auth.model.VerityType;
import com.exam.tomatoback.infrastructure.annotation.pass.PasswordMatches;
import com.exam.tomatoback.infrastructure.annotation.pass.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@PasswordMatches
public record ChangePasswordRequest(
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 값입니다.")
    String email,
    @NotNull(message = "인증 토큰은 필수입니다.")
    String token,
    @NotNull(message = "인증 타입은 필수입니다.")
    VerityType type,
    @ValidPassword
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    String password,
    @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
    String passwordConfirm
) implements PasswordMatchable {
  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getPasswordConfirm() {
    return this.passwordConfirm;
  }
}
