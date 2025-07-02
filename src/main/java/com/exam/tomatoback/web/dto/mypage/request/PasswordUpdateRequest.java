package com.exam.tomatoback.web.dto.mypage.request;

import com.exam.tomatoback.infrastructure.annotation.pass.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordUpdateRequest {
    @ValidPassword
    private String currentPassword;
    @NotBlank(message = "새로운 패스워드는 필수 값입니다.")
    private String newPassword;
    @NotBlank(message = "새로운 패스워드 확인 패스워드는 필수 값입니다.")
    private String confirmPassword;
}
