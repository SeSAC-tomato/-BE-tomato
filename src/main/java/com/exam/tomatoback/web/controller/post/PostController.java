package com.exam.tomatoback.web.controller.post;

import com.exam.tomatoback.post.repository.AddressRepository;
import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.post.post.PostCreateRequest;
import com.exam.tomatoback.web.dto.post.post.PostPageRequest;
import com.exam.tomatoback.web.dto.post.post.PostResponse;
import com.exam.tomatoback.web.dto.post.post.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AddressRepository addressRepository;

    //Post전체조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(CommonResponse.success(postService.getAllPostDeleteFalse(pageable)));
    }

    //Post 각종 필터
    @GetMapping("/search")
    public ResponseEntity<?> getSomePosts(
            @ModelAttribute PostPageRequest postPageRequest,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(CommonResponse.success(postService.getSomePostDeleteFalse(postPageRequest, pageable)));
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
    ){
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

    //끌올
    @PutMapping("/{id}/new")
    public ResponseEntity<?> pullPost(
            @PathVariable Long id ) {
        return ResponseEntity.ok(CommonResponse.success(postService.pullPost(id)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> progressPost(
            @PathVariable Long id ) {
        return ResponseEntity.ok(CommonResponse.success(postService.progressPost(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost (@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{postId}/cart")
    public ResponseEntity<?> setFavorite(@PathVariable Long postId){
        return ResponseEntity.ok(CommonResponse.success(postService.setFavorite(postId)));
    }

    @GetMapping("region")
    public ResponseEntity<?> getRegionInfo(){
        return ResponseEntity.ok(CommonResponse.success(postService.getAllDongs()));
    }
}
