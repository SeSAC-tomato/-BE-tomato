package com.exam.tomatoback.web.dto.mypage.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageHistoryResponse {
    private MyPostsPageResponse sellingPosts;
    private MyPostsPageResponse endPosts;
}

