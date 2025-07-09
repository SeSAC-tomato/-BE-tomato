package com.exam.tomatoback.web.dto.mypage.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank(message = "새로운 닉네임은 필수 값입니다.")
    private String nickname;

    @NotBlank(message = "새로운 주소값은 필수 값입니다.")    
    private String address;   // 전체 주소
    
    private String sido;      // 시/도
    private String sigungu;   // 시/군/구
    private String dong;      // 동/읍/면
    private Double x;         // 경도
    private Double y;         // 위도
}
