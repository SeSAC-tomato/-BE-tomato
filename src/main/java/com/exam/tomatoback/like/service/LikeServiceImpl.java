package com.exam.tomatoback.like.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.like.model.LikeSort;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.web.dto.like.request.CartGetRequest;
import com.exam.tomatoback.web.dto.like.response.CartGetResponse;
import com.exam.tomatoback.web.dto.like.response.CartPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {


    private final PostRepository postRepository;



    @Override
    public CartGetResponse getCartByUserId(Long userId, CartGetRequest request) {
        Pageable pageable = PageRequest.of(request.getCurrentPage(), request.getSize());
        List<CartPost> content = null;
        int currentPage=0;
        int totalPages=0;
        int size=0;
        long totalElements=0L;

        LikeSort sort = request.getLikeSort();

        // LIKE_SORT가 LIKED_AT인 경우
        if (sort == LikeSort.LIKED_AT) {

            Page<Post> posts = postRepository.findLikedPostsOrderByLikedAt(userId, pageable);
            if (posts.isEmpty()) {
                throw new TomatoException(TomatoExceptionCode.POSTS_NOT_FOUND_IN_MYPAGE);
            }
            content = posts.stream()
                    .map(post -> {
                        String imageUrl = post.getImages().stream()
                                .filter(Image::isMainImage)
                                .findFirst()
                                .map(Image::getUrl)
                                .orElse(null);

                        return CartPost.builder()
                                .title(post.getTitle())
                                .price(post.getPrice())
                                .img(imageUrl)
                                .createdAt(post.getCreatedAt())
                                .updatedAt(post.getUpdatedAt())
                                .status(post.getPostStatus())
                                .category(post.getPostCategory())
                                .build();
                    })
                    .toList();

            currentPage = posts.getNumber();
            totalPages = posts.getTotalPages();
            size = posts.getSize();
            totalElements = posts.getTotalElements();


            // LIKE_SORT가 POPULARITY인 경우. 인기순 정렬 (좋아요 수 내림차순)
        } else if (request.getLikeSort() == LikeSort.POPULARITY) {
            Page<Post> posts = postRepository.findLikedPostsOrderByPostLikeCountDesc(userId, pageable);

            if (posts.isEmpty()) {
                throw new TomatoException(TomatoExceptionCode.POSTS_NOT_FOUND_IN_MYPAGE);
            }
            content = mapPostsToCartPost(posts);
            currentPage = posts.getNumber();
            totalPages = posts.getTotalPages();
            size = posts.getSize();
            totalElements = posts.getTotalElements();

            // 나머지 정렬 기준 (createdAt, price 등). 게시글 작성일/가격 기준 정렬
        }  else if (request.getLikeSort() == LikeSort.CREATED_AT) {
            Sort typedSort = Sort.by(Sort.Direction.DESC, sort.getFieldPath());
            pageable = PageRequest.of(request.getCurrentPage(), request.getSize(), typedSort);

            Page<Post> posts = postRepository.findLikedPosts(userId, pageable);

            if (posts.isEmpty()) {
                throw new TomatoException(TomatoExceptionCode.POSTS_NOT_FOUND_IN_MYPAGE);
            }

            content = mapPostsToCartPost(posts);
            currentPage = posts.getNumber();
            totalPages = posts.getTotalPages();
            size = posts.getSize();
            totalElements = posts.getTotalElements();

        } else if (request.getLikeSort() == LikeSort.PRICE) {

            pageable = PageRequest.of(request.getCurrentPage(), request.getSize());

            Page<Post> posts = postRepository.findLikedPostsOrderByPrice(userId, pageable);

            if (posts.isEmpty()) {
                throw new TomatoException(TomatoExceptionCode.POSTS_NOT_FOUND_IN_MYPAGE);
            }

            content = mapPostsToCartPost(posts);
            currentPage = posts.getNumber();
            totalPages = posts.getTotalPages();
            size = posts.getSize();
            totalElements = posts.getTotalElements();

        }

        return CartGetResponse.builder()
                .currentPage(currentPage)
                .totalPages(totalPages)
                .size(size)
                .totalElements(totalElements)
                .likeSort(sort)
                .content(content)
                .build();
    }

    private List<CartPost> mapPostsToCartPost(Page<Post> posts) {
        return posts.stream()
                .map(post -> {
                    String imageUrl = post.getImages().stream()
                            .filter(Image::isMainImage)
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null);

                    return CartPost.builder()
                            .title(post.getTitle())
                            .price(post.getPrice())
                            .img(imageUrl)
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .status(post.getPostStatus())
                            .category(post.getPostCategory())
                            .build();
                })
                .toList();
    }

}
