package com.exam.tomatoback.chat.validator;

import com.exam.tomatoback.chat.annotation.ValidChatRequest;
import com.exam.tomatoback.web.dto.chat.websocket.ChatRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class ChatRequestValidator  implements ConstraintValidator<ValidChatRequest, ChatRequest> {
    @Override
    public boolean isValid(ChatRequest chatRequest, ConstraintValidatorContext context) {
        // Type -> message, image, book, end

        // message -> 단순 메시지
        // content 비어있으면 안됨

        // image -> 이미지
        // content -> 비어있어도 됨

        // book -> 예약 이벤트
        //

        // end -> 완료 이벤트
        //


        switch (chatRequest.getChatType()) {
            case MESSAGE -> {
                boolean hasContent = StringUtils.hasText(chatRequest.getContent());
                if (!hasContent) {
                    return false;
                }
            }
            case IMAGE -> {
                boolean hasImages = chatRequest.getImages() != null && !chatRequest.getImages().isEmpty();
                if(!hasImages){
                    return false;
                }
            }
            case EVENT_BOOK, EVENT_END -> {
                Long targetId = chatRequest.getTargetId();
                if(targetId == null){
                    return false;
                }
            }
        }







        return true;
    }


}
