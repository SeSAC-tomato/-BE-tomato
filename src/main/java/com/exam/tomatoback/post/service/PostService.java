package com.exam.tomatoback.post.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.like.model.Like;
import com.exam.tomatoback.like.repository.LikeRepository;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;

import com.exam.tomatoback.post.repository.ImageRepository;
import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.user.repository.UserRepository;

import com.exam.tomatoback.web.dto.post.*;

import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;
    private final LikeRepository likeRepository;

//    @Value("${file.upload.dir}")
//    private String uploadDir;
//    private static final String IMAGE_BASE_URL_PATH = "/upload/images/";

    //Post 전체조회 (소프트 삭제 제외)
    public List<PostResponse> getAllPostDeleteFalse() {
        return postRepository.findAllByDeletedFalse().stream().map(PostResponse::from).toList();
    }

    //Post 개별조회 (소프트 삭제제외)
    public PostResponse getPostById(Long id) {
       return postRepository.findByIdAndDeletedFalse(id).map(PostResponse::from)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }


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
        return PostResponse.from(savedPost);
    }


    //Post수정
    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request){
        Post updatePost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        updatePost.setTitle(request.getTitle());
        updatePost.setPrice(request.getPrice());
        updatePost.setContent(request.getContent());
        updatePost.setPostStatus(request.getPostStatus());
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

    //끌올 (Update날짜를 갱신하여 날짜기준으로 올림? 끌올 표시를 하고 싶었다면 .. 별도의 테이블과 로직필요)
    @Transactional
    public PostResponse pullPost(Long id){
        Post pullPost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        pullPost.setPrice(pullPost.getPrice());
        return PostResponse.from(postRepository.save(pullPost));
    }

    // Status DB로 수정
//    @Transactional
//    public PostResponse changeStatus(Long id){
//        Post targetPost = postRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new TomatoException(
//                TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
//
//        PostStatus currentStatus = targetPost.getStatus();
//        PostStatus nextStatus = currentStatus.nextStatus();
//        if(nextStatus == null){
//            throw new TomatoException(TomatoExceptionCode.STATUS_UPDATE_FAILURE);
//        }
//        targetPost.setStatus(nextStatus);
//        return PostResponse.from(postRepository.save(targetPost));
//    }

    //일부조회 (이미지 포함, 소프트 삭제제외)
    public PostResponse getPostByIdDeleteFalse(Long id) {
         return postRepository.findByIdAndDeletedFalse(id).map(PostResponse::from)
            .orElseThrow(() -> new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }


    //충돌방지를 위해서 Post에 일단 생성
    public LikeResponse addFavorite(Long postId){
        Post favoritePost = postRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(() -> new TomatoException(
                        TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
        User currentUser = getCurrentUser();
        Long userId = currentUser.getId();

        Like newLike = Like.builder().user(currentUser).post(favoritePost).build();
        Like savedLike = likeRepository.save(newLike);
        return LikeResponse.from(savedLike);
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