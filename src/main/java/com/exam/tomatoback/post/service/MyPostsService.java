package com.exam.tomatoback.post.service;

import com.exam.tomatoback.web.dto.mypage.request.MyPageHistoryGetRequest;
import com.exam.tomatoback.web.dto.mypage.response.MyPageHistoryResponse;
import org.springframework.stereotype.Service;

public interface MyPostsService {

    MyPageHistoryResponse getMyPostsByUserId(Long userId, MyPageHistoryGetRequest request);
}
