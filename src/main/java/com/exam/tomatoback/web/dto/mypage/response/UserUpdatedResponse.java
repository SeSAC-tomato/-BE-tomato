package com.exam.tomatoback.web.dto.mypage.response;

import lombok.*;


public record UserUpdatedResponse(
    String nickname,
    String address
){}