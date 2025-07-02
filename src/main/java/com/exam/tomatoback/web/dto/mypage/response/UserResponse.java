package com.exam.tomatoback.web.dto.mypage.response;

import lombok.*;


public record UserResponse(
        String nickname,
        String email,
        String address
){}