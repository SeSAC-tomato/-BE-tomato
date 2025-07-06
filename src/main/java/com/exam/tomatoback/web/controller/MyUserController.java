package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.web.dto.like.request.*;
import com.exam.tomatoback.like.service.LikeService;
import com.exam.tomatoback.post.service.MyPostsService;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.like.request.CartGetRequest;
import com.exam.tomatoback.web.dto.like.request.LikeSort;
import com.exam.tomatoback.web.dto.mypage.request.MyPageHistoryGetRequest;
import com.exam.tomatoback.web.dto.mypage.request.PasswordUpdateRequest;
import com.exam.tomatoback.web.dto.mypage.request.UserUpdateRequest;
import com.exam.tomatoback.web.dto.like.response.CartGetResponse;
import com.exam.tomatoback.web.dto.mypage.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class MyUserController {

    private final UserService userService;
    private final LikeService likeService;
    private final MyPostsService myPostsService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        try {
            User user = userService.getCurrentUser();
            System.out.println("Current user: " + user.getEmail() + ", ID: " + user.getId());
            UserInfoResponse response = UserInfoResponse.from(user);
            return ResponseEntity.ok(CommonResponse.success(response));
        } catch (Exception e) {
            System.err.println("Error in getMyInfo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserById(
            @PathVariable Long userId
    ){
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateUserById(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequest request
    ){
        UserUpdatedResponse response = userService.updateUserById(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePasswordById(
            @PathVariable Long userId,
            @RequestBody PasswordUpdateRequest request
            ){
        PasswordUpdatedResponse response = userService.updatePasswordById(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{userId}/cart")
    public ResponseEntity<?> getCartById(
            @PathVariable Long userId,
            @RequestParam(value = "currentPage", required = false, defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "likeSort", required = false, defaultValue = "LIKE_CREATED_AT") String likeSortStr
    ){

        LikeSort likeSort;
        try {
            likeSort = LikeSort.valueOf(likeSortStr);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 정렬 기준입니다.");
        }


        CartGetRequest request = CartGetRequest.builder()
                .currentPage(currentPage)
                .size(size)
                .likeSort(likeSort)
                .build();

        CartGetResponse response = likeService.getCartByUserId(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @DeleteMapping("/{userId}/cart/{postId}")
    public ResponseEntity<?> deleteCartByPostId(@PathVariable Long userId, @PathVariable Long postId){
        likeService.deleteLike(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/posts/myPosts")
    public ResponseEntity<?> getMyPosts(
            @PathVariable Long userId,
            @RequestParam(value = "currentSellingPage", required = false, defaultValue = "0") Integer currentSellingPage,
            @RequestParam(value = "sellingSize", required = false, defaultValue = "10") Integer sellingSize,
            @RequestParam(value = "currentSoldPage", required = false, defaultValue = "0") Integer currentSoldPage,
            @RequestParam(value = "soldSize", required = false, defaultValue = "10") Integer soldSize

    ) {
        MyPageHistoryGetRequest request = MyPageHistoryGetRequest.builder()
                .currentSellingPage(currentSellingPage)
                .currentSoldPage(currentSoldPage)
                .sellingSize(sellingSize)
                .soldSize(soldSize)
                .build();

        MyPageHistoryResponse response = myPostsService.getMyPostsByUserId(userId, request);
        return ResponseEntity.ok(CommonResponse.success(response));

    }

}
