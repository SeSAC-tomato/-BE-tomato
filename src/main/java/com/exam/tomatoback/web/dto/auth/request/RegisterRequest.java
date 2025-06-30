package com.exam.tomatoback.web.dto.auth.request;

import com.exam.tomatoback.infrastructure.annotation.pass.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
public class RegisterRequest {
  @Email
  @NotBlank
  private String email;
  @ValidPassword
  @NotBlank
  private String password;
  @NotBlank
  private String passwordConfirm;
  @NotBlank
  @Size(min = 2, max = 10)
  private String nickname;
  @NotBlank
  private String address;
}
