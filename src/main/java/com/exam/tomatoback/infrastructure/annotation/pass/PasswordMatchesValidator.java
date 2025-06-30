package com.exam.tomatoback.infrastructure.annotation.pass;

import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getPasswordConfirm() == null) {
            return false;
        }

        boolean match = request.getPassword().equals(request.getPasswordConfirm());

        if (!match) {
            // 필드에 메시지를 지정하고 싶을 경우
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")
                    .addPropertyNode("passwordConfirm")
                    .addConstraintViolation();
        }

        return match;
    }
}
