package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.post.ImageMetadataRequest;
import com.exam.tomatoback.web.dto.post.PostCreateRequest;
import com.exam.tomatoback.web.dto.post.PostResponse;
import com.exam.tomatoback.web.dto.post.PostUpdateRequest;
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.success(postService.getPostByIdDeleteFalse(id)));
    }

    //Post생성
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestBody PostCreateRequest postCreateRequest
//            @RequestPart("postRequest") PostCreateRequest postCreateRequest,
//            @RequestPart(name="imageFiles", required=true) List<MultipartFile> imageFiles,
//            @RequestPart(name="imageMetadata",required=true ) List<ImageMetadataRequest> imageMetadatas
    ){
//         PostResponse postResponse = postService.createPost(postCreateRequest, imageFiles, imageMetadatas);
        PostResponse postResponse = postService.createPost(postCreateRequest);
        return ResponseEntity.ok(CommonResponse.success(postResponse));
    }

    //PostUpdate
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest updateRequest){
        return ResponseEntity.ok(CommonResponse.success(postService.updatePost(id, updateRequest)));
    }

    @PutMapping("/{id}/new")
    public ResponseEntity<?> pullPost(
            @PathVariable Long id ) {
        return ResponseEntity.ok(CommonResponse.success(postService.pullPost(id)));
    }

    //PostDB쪽으로
//    @PutMapping("/{id}/status")
//    public ResponseEntity<?> changeStatus(
//            @PathVariable Long id ) {
//        return ResponseEntity.ok(CommonResponse.success(postService.changeStatus(id)));
//    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost (@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    //충돌방지를 위해 일단 여기에 저장
    @PostMapping("/{postId}/cart")
    public ResponseEntity<?> addFavorites(@PathVariable Long postId, @RequestBody Long userId){
        return ResponseEntity.ok(CommonResponse.success(postService.addFavorite (postId,userId)));
    }


}
