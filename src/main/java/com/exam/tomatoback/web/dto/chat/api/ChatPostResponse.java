package com.exam.tomatoback.web.dto.chat.api;

import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPostResponse {
    private long id;
    private long userId;
    private String nickname;

    private String title;
    private String content;
    private long price;


    private PostStatus postStatus;
    private ProductCategory productCategory;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
}
