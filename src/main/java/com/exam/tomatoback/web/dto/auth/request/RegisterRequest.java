package com.exam.tomatoback.web.dto.auth.request;

import com.exam.tomatoback.infrastructure.annotation.pass.PasswordMatches;
import com.exam.tomatoback.infrastructure.annotation.pass.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class RegisterRequest implements PasswordMatchable {
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 항목입니다.")
  private String email;
  @ValidPassword
  @NotBlank(message = "비밀번호는 필수 항목입니다.")
  private String password;
  @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
  private String passwordConfirm;
  @NotBlank(message = "닉네임은 필수 항목입니다.")
  @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이어야 합니다.")
  private String nickname;
  @NotBlank(message = "주소는 필수 항목입니다.")
  private String address;
  @NotBlank(message = "도/시 이름은 필수 항목입니다.")
  private String sido;
  @NotBlank(message = "시/군/구 이름은 필수 항목입니다.")
  private String sigungu;
  @NotBlank(message = "법정동/법정리 이름은 필수 항목입니다.")
  private String dong;
}
