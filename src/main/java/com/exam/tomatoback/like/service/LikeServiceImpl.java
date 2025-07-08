package com.exam.tomatoback.like.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.like.model.Like;
import com.exam.tomatoback.like.repository.LikeRepository;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.like.request.CartGetRequest;
import com.exam.tomatoback.web.dto.like.request.LikeSort;
import com.exam.tomatoback.web.dto.like.response.CartGetResponse;
import com.exam.tomatoback.web.dto.like.response.CartPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {


    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeRepository likeRepository;

    @Override
    public CartGetResponse getCartByUserId(Long userId, Integer currentPage, Integer size, LikeSort likeSort){

//        if (!currentUser.getId().equals(userId)) { throw new TomatoException(TomatoExceptionCode.USER_MISMATCH_IN_MYPAGE);}
        CartGetRequest request = CartGetRequest.builder()
                .currentPage(currentPage)
                .size(size)
                .likeSort(likeSort)
                .build();

        Pageable pageable = PageRequest.of(request.getCurrentPage(), request.getSize());
        Page<Post> posts;



        // 분기: 정렬 방식에 따라 posts만 다르게 가져옴
        switch (request.getLikeSort()) {
            case LIKE_CREATED_AT -> posts = postRepository.findLikedPostsOrderByLikedAt(userId, pageable);
            case POPULARITY -> posts = postRepository.findLikedPostsOrderByPostLikeCountDesc(userId, pageable);
            case POST_UPDATED_AT -> {
                pageable = PageRequest.of(request.getCurrentPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "updatedAt"));
                posts = postRepository.findLikedPostsOrderByPostUpdatedAt(userId, pageable);
            }
            case PRICE -> posts = postRepository.findLikedPostsOrderByPrice(userId, pageable);
            default -> throw new TomatoException(TomatoExceptionCode.INVALID_SORT_OPTION_IN_MYPAGE);
        }

        // 공통 처리
        if (posts.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.POSTS_NOT_FOUND_IN_MYPAGE);
        }

        List<CartPost> content = mapPostsToCartPost(posts);

        return CartGetResponse.builder()
                .currentPage(posts.getNumber())
                .totalPages(posts.getTotalPages())
                .size(posts.getSize())
                .totalElements(posts.getTotalElements())
                .likeSort(likeSort)
                .content(content)
                .build();
    }

    @Override
    public void deleteLike(Long userId, Long postId) {

        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
        .orElseThrow(() -> new TomatoException(TomatoExceptionCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }


    private List<CartPost> mapPostsToCartPost(Page<Post> posts) {
        return posts.stream()
                .map(post -> {
                    String imageUrl = post.getImages().stream()
                            .filter(Image::getMainImage)
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null);

                    CartPost cartPost = CartPost.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .img(imageUrl)
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .postStatus(post.getPostProgress().getPostStatus())
                            .productCategory(post.getProductCategory())
                            .build();
                    
                    // System.out.println("Created CartPost with postId: " + cartPost.getPostId() + ", title: " + cartPost.getTitle());
                    
                    return cartPost;
                })
                .toList();
    }

}
