package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.mypage.service.MyUserService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.myPageDto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class MyUserController {

    private final MyUserService userService   ;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(
            @PathVariable Long userId
    ){
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(CommonResponse.success(user));
    }


}
