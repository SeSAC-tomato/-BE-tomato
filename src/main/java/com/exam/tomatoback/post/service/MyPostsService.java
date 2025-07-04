package com.exam.tomatoback.post.service;

import com.exam.tomatoback.web.dto.mypage.request.MyPageHistoryGetRequest;
import com.exam.tomatoback.web.dto.mypage.response.MyPageHistoryResponse;

public interface MyPostsService {

    MyPageHistoryResponse getMyPostsByUserId(Long userId, MyPageHistoryGetRequest request);
}
