package com.exam.tomatoback.chat.annotation;


import com.exam.tomatoback.chat.validator.ChatRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChatRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChatRequest {
    String message() default "디폴트 에러메시지 ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
