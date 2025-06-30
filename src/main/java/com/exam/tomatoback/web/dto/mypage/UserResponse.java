package com.exam.tomatoback.web.dto.mypage;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String nickname;
    private String email;
    private String address;
}
