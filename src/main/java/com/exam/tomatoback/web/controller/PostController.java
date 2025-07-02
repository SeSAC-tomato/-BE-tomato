package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.post.ImageMetadataRequest;
import com.exam.tomatoback.web.dto.post.PostCreateRequest;
import com.exam.tomatoback.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //Post전체조회
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(CommonResponse.success(postService.getAllPostDeleteFalse()));
    }

    //Post개별조회
    @GetMapping
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.success(postService.getPostByIdDeleteFalse(id)));
    }

    //Post생성
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestPart("postRequest") PostCreateRequest postCreateRequest,
            @RequestPart(name="imageFiles", required=true) List<MultipartFile> imageFiles,
            @RequestPart(name="imageMetadata",required=true ) List<ImageMetadataRequest> imageMetadatas
    ){
         PostResponse postResponse = postService.createPost(postCreateRequest, imageFiles, imageMetadatas);
         return ResponseEntity.ok(CommonResponse.success(postResponse));
    }




}
