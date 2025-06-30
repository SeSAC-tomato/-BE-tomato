package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.mypage.service.MyUserService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.mypage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class MyUserController {

    private final MyUserService userService   ;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<CommonResponse<UserResponse>> getUserById(
            @PathVariable Long userId
    ){
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<CommonResponse<UserUpdatedResponse>> updateUserById(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest request
    ){
        UserUpdatedResponse response = userService.updateUserById(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<CommonResponse<PasswordUpdatedResponse>> updatePasswordById(
            @PathVariable Long userId,
            @RequestBody PasswordUpdateRequest request
            ){
        PasswordUpdatedResponse response = userService.updatePasswordById(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
