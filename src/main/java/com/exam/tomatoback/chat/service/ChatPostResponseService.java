package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.web.dto.chat.api.ChatPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatPostResponseService {
    public ChatPostResponse from(Post post) {
        ChatPostResponse response = new ChatPostResponse();
        response.setId(post.getId());
        response.setUserId(post.getUser().getId());
        response.setNickname(post.getUser().getNickname());

        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPrice(post.getPrice());

        response.setPostStatus(post.getPostProgress().getPostStatus());
        response.setProductCategory(post.getProductCategory());

        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setImages(post.getImages().stream().map(Image::getSavedName).toList());

        return response;
    }
}
