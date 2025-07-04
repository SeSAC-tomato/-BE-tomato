package com.exam.tomatoback.web.dto.mypage.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostsPageResponse {
    private List<MyPost> content;
    private PageMeta pageMeta;
}
