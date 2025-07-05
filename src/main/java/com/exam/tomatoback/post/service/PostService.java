package com.exam.tomatoback.post.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Like;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostProgress;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.repository.LikeRepository;
import com.exam.tomatoback.post.repository.PostProgressRepository;
import com.exam.tomatoback.user.model.User;

import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.user.repository.UserRepository;

import com.exam.tomatoback.web.dto.post.like.LikeResponse;
import com.exam.tomatoback.web.dto.post.post.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.awt.print.Pageable;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
//    private final ImageRepository imageRepository;
//    private final ResourceLoader resourceLoader;
    private final LikeRepository likeRepository;
    private final PostProgressRepository postProgressRepository;

//    @Value("${file.upload.dir}")
//    private String uploadDir;
//    private static final String IMAGE_BASE_URL_PATH = "/upload/images/";

    //Post 전체조회 (소프트 삭제 제외)
    public List<PostResponse> getAllPostDeleteFalse() {
        return postRepository.findAllByDeletedFalse().stream().map(PostResponse::from).toList();
    }

    //Post 일부조회 (소프트삭제 제외)

    public Page<PostResponse> getSomePostDeleteFalse(PostPageRequest postPageRequest, Pageable pageable) {
        Page<Post> post = postRepository.searchWithFiltersDeleteFalse(postPageRequest, pageable);

        // 엔티티 → DTO 변환

    }

    //ID로 조회 (소프트 삭제제외)
    public PostResponseWithOwner getPostByIdDeleteFalse(Long id) {
        return postRepository.findByIdAndDeletedFalse(id).map(PostResponseWithOwner::from)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }

//    public PostResponse getSomePostDeleteFalse(PostPageRequest postPageRequest){
//        return postRepository.
//    }

    //Post 생성
    @Transactional
    public PostResponse createPost(PostCreateRequest postCreateRequest
//                                   ,List<MultipartFile> imageFiles,
//                                   List<ImageMetadataRequest> imageMetadatas
    ) {

        //DB로 보낼 Post를 생성 (User설정 후 커밋대기)
        User currentUser = getCurrentUser();
        Post post = postCreateRequest.toDomain();
        post.setUser(currentUser);
        Post savedPost = postRepository.save(post);
        PostProgress initialPostProgress = PostProgress.builder()
                .user(currentUser) // PostProgress에도 User 정보 연결
                .build();
        initialPostProgress.setPost(savedPost);
        postProgressRepository.save(initialPostProgress);
        post.setPostProgress(initialPostProgress);
        Post finalPost = postRepository.save(post);

        //이미지 처리
//        if (imageFiles == null || imageFiles.isEmpty()) {
//            throw new TomatoException(TomatoExceptionCode.IMAGE_NOT_FOUND);
//        }
//        if (imageMetadatas == null || imageMetadatas.isEmpty()) {
//            throw new TomatoException(TomatoExceptionCode.MAIN_IMAGE_NOT_FOUND);
//        }
//
//        if(imageFiles.size() != imageMetadatas.size()){
//            throw new TomatoException(TomatoExceptionCode.IMAGE_INFO_NOT_MATCH);
//        }
//
//        Map<String, ImageMetadataRequest> imageMetadataMap = imageMetadatas
//                .stream()
//                .collect(Collectors.toMap(ImageMetadataRequest::getOriginalFileName, metadata -> metadata));
//
//        List<Image> imagesToSave = new ArrayList<>();
//
//        for (int i = 0; i < imageFiles.size(); i++) {
//            MultipartFile file = imageFiles.get(i);
//            String uuid = UUID.randomUUID().toString();
//            String originalNameFromMultipart = file.getOriginalFilename();
//            String originalNameSafe = (originalNameFromMultipart != null) ? originalNameFromMultipart : "unknown_file";
//            String extension = "";
//            if (originalNameSafe.lastIndexOf(".") != -1) {
//                extension = originalNameSafe.substring(originalNameSafe.lastIndexOf("."));
//            }
//
//            String savedFileName = uuid + extension;
//            Path fileSavePath = Paths.get(uploadDir, savedFileName);
//
//            try{
//                Files.copy(file.getInputStream(), fileSavePath);
//            }catch (IOException e){
//                throw new TomatoException(TomatoExceptionCode.IMAGE_PROCESS_FAILURE);
//            }
//
//            boolean isMainBoolean = ( i == 0 );
//            Image image = Image.builder()
//                    .url(IMAGE_BASE_URL_PATH)
//                    .savedName(savedFileName)
//                    .originalName(originalNameSafe)
//                    .mainImage(isMainBoolean)
//                    .post(savedPost)
//                    .build();
//
//            imagesToSave.add(image);
//        }
//        imageRepository.saveAll(imagesToSave);
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

        PostStatus currentStatus = targetPost.getPostProgress().getStatus();
        PostStatus nextStatus = currentStatus.nextStatus();
        if(nextStatus == null){
            throw new TomatoException(TomatoExceptionCode.STATUS_UPDATE_FAILURE);
        }
        targetPost.getPostProgress().setStatus(nextStatus);
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

    //충돌방지를 위해서 Post에 일단 생성 (Like)
    public LikeResponse setFavorite(Long postId){
        Post postFavorite = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        User currentUser = getCurrentUser();
        Optional<Like> existingLike = likeRepository.findByPostAndUser( postFavorite, currentUser);

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
}