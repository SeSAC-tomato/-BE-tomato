package com.exam.tomatoback.post.service;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;

import com.exam.tomatoback.post.repository.ImageRepository;
import com.exam.tomatoback.post.repository.PostRepository;
import com.exam.tomatoback.user.repository.UserRepository;

import com.exam.tomatoback.web.dto.post.ImageMetadataRequest;
import com.exam.tomatoback.web.dto.post.PostCreateRequest;
import com.exam.tomatoback.web.dto.post.PostResponse;

import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ResourceLoader resourceLoader;

    @Value("${file.upload.dir}")
    private String uploadDir;
    private static final String IMAGE_BASE_URL_PATH = "/upload/images/";

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
    public PostResponse createPost(PostCreateRequest postCreateRequest,
                                   List<MultipartFile> imageFiles,
                                   List<ImageMetadataRequest> imageMetadatas) {

        //DB로 보낼 Post를 생성 (User설정 후 커밋대기)
        User currentUser = getCurrentUser();
        Post post = postCreateRequest.toDomain();
        post.setUser(currentUser);
        Post savedPost = postRepository.save(post);

        //이미지 처리
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.IMAGE_NOT_FOUND);
        }
        if (imageMetadatas == null || imageMetadatas.isEmpty()) {
            throw new TomatoException(TomatoExceptionCode.MAIN_IMAGE_NOT_FOUND);
        }

        if(imageFiles.size() != imageMetadatas.size()){
            throw new TomatoException(TomatoExceptionCode.IMAGE_INFO_NOT_MATCH);
        }

        Map<String, ImageMetadataRequest> imageMetadataMap = imageMetadatas
                .stream()
                .collect(Collectors.toMap(ImageMetadataRequest::getOriginalFileName, metadata -> metadata));

        List<Image> imagesToSave = new ArrayList<>();

        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile file = imageFiles.get(i);
            String uuid = UUID.randomUUID().toString();
            String originalNameFromMultipart = file.getOriginalFilename();
            String originalNameSafe = (originalNameFromMultipart != null) ? originalNameFromMultipart : "unknown_file";
            String extension = "";
            if (originalNameSafe.lastIndexOf(".") != -1) {
                extension = originalNameSafe.substring(originalNameSafe.lastIndexOf("."));
            }

            String savedFileName = uuid + extension;
            Path fileSavePath = Paths.get(uploadDir, savedFileName);

            try{
                Files.copy(file.getInputStream(), fileSavePath);
            }catch (IOException e){
                throw new TomatoException(TomatoExceptionCode.IMAGE_PROCESS_FAILURE);
            }

            boolean isMainBoolean = ( i == 0 );
            Image image = Image.builder()
                    .url(IMAGE_BASE_URL_PATH)
                    .savedName(savedFileName)
                    .originalName(originalNameSafe)
                    .mainImage(isMainBoolean)
                    .post(savedPost)
                    .build();

            imagesToSave.add(image);
        }
        imageRepository.saveAll(imagesToSave);
        return PostResponse.from(savedPost);
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

    //전체조회 (이미지 포함, 소프트 삭제제외)
    public PostResponse getPostByIdDeleteFalse(Long id) {
         return postRepository.findByIdAndDeletedFalse(id).map(PostResponse::from)
            .orElseThrow(() -> new TomatoException(
                    TomatoExceptionCode.ASSOCIATED_POST_NOT_FOUND));
    }

}