package com.exam.tomatoback.web.dto.mypage.response;

import com.exam.tomatoback.user.model.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(
        Long id,
        String nickname,
        String email,
        String address

) {
    public static UserInfoResponse from(User user) {

        return UserInfoResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .address(user.getAddress().getAddress())
                .build();
    }
}

