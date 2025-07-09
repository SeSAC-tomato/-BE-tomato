package com.exam.tomatoback.post.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.web.dto.like.request.LikeSort;
import com.exam.tomatoback.web.dto.like.response.CartGetResponse;
import com.exam.tomatoback.web.dto.like.response.CartPost;
import com.exam.tomatoback.web.dto.mypage.request.MyPageHistoryGetRequest;
import com.exam.tomatoback.web.dto.mypage.response.MyPageHistoryResponse;
import com.exam.tomatoback.web.dto.mypage.response.MyPost;
import com.exam.tomatoback.web.dto.mypage.response.MyPostsPageResponse;
import com.exam.tomatoback.web.dto.mypage.response.PageMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPostsServiceImpl implements MyPostsService {
    private final PostRepository postRepository;

    @Override
    public MyPageHistoryResponse getMyPostsByUserId(Long userId, MyPageHistoryGetRequest request) {
        Pageable sellingPageable = PageRequest.of(request.getCurrentSellingPage(), request.getSellingSize());
        Pageable endPageable = PageRequest.of(request.getCurrentSellingPage(), request.getSoldSize());

        Page<Post> mySellingPosts = postRepository.findSellingPostsByUserId(userId, sellingPageable);
        Page<Post> myEndPosts = postRepository.findEndPostsByUserId(userId, endPageable);



        if (mySellingPosts.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.SELLING_POSTS_NOT_FOUND_IN_MYPAGE);
        }
        if (myEndPosts.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.END_POSTS_NOT_FOUND_IN_MYPAGE);
        }

        MyPostsPageResponse sellingPosts = convertToMyPostsPageResponse(mySellingPosts);
        MyPostsPageResponse endPosts = convertToMyPostsPageResponse(myEndPosts);



        return MyPageHistoryResponse.builder()
                .sellingPosts(sellingPosts)
                .endPosts(endPosts)
                .totalSellingPosts(sellingPosts.getPageMeta().getTotalElements())
                .totalEndPosts(endPosts.getPageMeta().getTotalElements())
                .build();
    }


    private List<MyPost> mapPostsToMyPost(Page<Post> posts) {
        return posts.stream()
                .map(post -> {
                    String imageUrl = post.getImages().stream()
                            .filter(Image::getMainImage)
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null);

                    return MyPost.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .img(imageUrl)
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .postStatus(post.getPostProgress().getPostStatus())
                            .productCategory(post.getProductCategory())
                            .build();
                })
                .toList();
    }

    private MyPostsPageResponse convertToMyPostsPageResponse(Page<Post> postPage) {
        List<MyPost> content = mapPostsToMyPost(postPage);
        PageMeta pageMeta = PageMeta.builder()
                .currentPage(postPage.getNumber())
                .totalPages(postPage.getTotalPages())
                .size(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .build();

        return MyPostsPageResponse.builder()
                .content(content)
                .pageMeta(pageMeta)
                .build();
    }
}
