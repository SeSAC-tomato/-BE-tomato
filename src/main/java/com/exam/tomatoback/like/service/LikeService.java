package com.exam.tomatoback.like.service;

import com.exam.tomatoback.web.dto.like.request.CartGetRequest;
import com.exam.tomatoback.web.dto.like.response.CartGetResponse;

public interface LikeService {

    // userId로 해당 사용자 관심목록 가져오기
    CartGetResponse getCartByUserId(Long userId, CartGetRequest request);

    void deleteLike(Long userId, Long postId);
}
