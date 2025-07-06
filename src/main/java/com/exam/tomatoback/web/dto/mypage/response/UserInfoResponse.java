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
        String address = null;
        if (user.getAddress() != null) {
            address = user.getAddress().getAddress();
        }

        return UserInfoResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .address(address)
                .build();
    }
}

