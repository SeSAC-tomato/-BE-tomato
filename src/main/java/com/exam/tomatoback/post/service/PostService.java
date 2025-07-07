package com.exam.tomatoback.post.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.like.model.Like;
import com.exam.tomatoback.like.repository.LikeRepository;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostProgress;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.repository.ImageRepository;
import com.exam.tomatoback.post.repository.PostProgressRepository;
import com.exam.tomatoback.user.model.User;

import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.user.repository.UserRepository;

import com.exam.tomatoback.web.dto.like.request.LikeResponse;
import com.exam.tomatoback.web.dto.post.image.ImageCreateRequest;
import com.exam.tomatoback.web.dto.post.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final PostProgressRepository postProgressRepository;

//    @Value("${file.upload.dir}")
//    private String uploadDir;
//    private static final String IMAGE_BASE_URL_PATH = "/upload/images/";

    //Post 전체조회 (소프트 삭제 제외)
    public PostPageResponse getAllPostDeleteFalse(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByDeletedFalse(pageable);
        return PostPageResponse.from(postPage);
    }

    //Post 일부조회 (소프트삭제 제외)
    public PostPageResponse getSomePostDeleteFalse(PostPageRequest postPageRequest, Pageable pageable) {
        Page<Post> postPage = postRepository.searchWithFiltersDeleteFalse(postPageRequest, pageable);
        return PostPageResponse.from(postPage);
    }

    //ID로 조회 (소프트 삭제제외)
    public PostResponseWithOwner getPostByIdDeleteFalse(Long id) {
        Post post = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));

        return postRepository.findByIdAndDeletedFalse(id).map(PostResponseWithOwner::from)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }

    //Post 생성
    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest) {

        //DB로 보낼 Post를 생성 (User설정 후 커밋대기)
        User currentUser = getCurrentUser();
        Post post = postCreateRequest.toDomain();
        post.setUser(currentUser);
        Post savedPost = postRepository.save(post);
        PostProgress initialPostProgress = PostProgress.builder()
                .user(currentUser) // PostProgress에도 User 정보 연결
                .post(savedPost)
                .postStatus(PostStatus.SELLING)
                .build();
        initialPostProgress.setPost(savedPost);
        postProgressRepository.save(initialPostProgress);
        post.setPostProgress(initialPostProgress);
        Post finalPost = postRepository.save(post);
//
        if (postCreateRequest.getImageInfo() != null && !postCreateRequest.getImageInfo().isEmpty()) {
            List<Image> imagesToSave = new ArrayList<>();
            for (ImageCreateRequest imageRequest : postCreateRequest.getImageInfo()) {
                // Image 엔티티 생성
                String savedName = imageRequest.getSavedName();
                String originalName = imageRequest.getOriginalName();
                Boolean mainImage = imageRequest.getMainImage();
                String url = Constants.POST_IMAGE_DIR;
                Image postImage = Image.builder()
                        .savedName(imageRequest.getSavedName())
                        .originalName(imageRequest.getOriginalName())
                        .mainImage(imageRequest.getMainImage())
                        .post(finalPost)
                        .url(url)
                        .build();
                imagesToSave.add(postImage);
            }
            imageRepository.saveAll(imagesToSave);
        }
        return PostResponse.from(finalPost);
    }


    //Post수정
    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request){
        Post updatePost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        updatePost.setTitle(request.getTitle());
        updatePost.setPrice(request.getPrice());
        updatePost.setContent(request.getContent());
        updatePost.setProductCategory(request.getProductCategory());
        updatePost.setProductCategory(request.toDomain().getProductCategory());
        return PostResponse.from(postRepository.save(updatePost));
    }

    //Post삭제
    @Transactional
    public void deletePost(Long id){
        Post softDeletePost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        softDeletePost.setDeleted(true);
    }

    //Post_Progress
@Transactional
    public PostResponse progressPost(Long id){
        Post targetPost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));

        PostStatus currentStatus = targetPost.getPostProgress().getPostStatus();
        PostStatus nextStatus = currentStatus.nextStatus();
        if(nextStatus == null){
            throw new TomatoException(TomatoExceptionCode.STATUS_UPDATE_FAILURE);
        }
        targetPost.getPostProgress().setPostStatus(nextStatus);
        return PostResponse.from(postRepository.save(targetPost));
    }

    //끌올 (Update날짜를 갱신하여 날짜기준으로 올림? 끌올 표시를 하고 싶었다면 .. 별도의 테이블과 로직필요)
    @Transactional
    public PostResponse pullPost(Long id){
        Post pullPost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        pullPost.setPrice(pullPost.getPrice());
        return PostResponse.from(postRepository.save(pullPost));
    }



    //User정보 조회
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TomatoException(TomatoExceptionCode.USER_NOT_FOUND);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();

            // UserRepository를 사용하여 DB에서 실제 User 엔티티를 조회합니다.
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));
        } else {
            throw new TomatoException(TomatoExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Post getPostNoMatterWhatById(long id){
        return postRepository.findById(id).orElseThrow(() -> new TomatoException(TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }

    public List<Post> getSellingPostByUserId(long userId){

        return postRepository.findByUserIdAndPostStatus(userId, PostStatus.SELLING);
    }

    public LikeResponse setFavorite(Long postId){
        Post postFavorite = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        User currentUser = getCurrentUser();
        Optional<Like> existingLike = likeRepository.findByPostAndUser(postFavorite, currentUser);

        if(existingLike.isPresent()){
            Like likeToDelete = existingLike.get();
            likeRepository.delete(likeToDelete);
            return LikeResponse.from(likeToDelete, false);
        } else {
            Like newLike = Like.builder().user(currentUser).post(postFavorite).build();
            Like savedLike = likeRepository.save(newLike);
            return LikeResponse.from(savedLike, true);
        }
    }

}