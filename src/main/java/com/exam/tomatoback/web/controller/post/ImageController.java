package com.exam.tomatoback.web.controller.post;

import com.exam.tomatoback.infrastructure.util.PostImageServer;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.post.image.ImageUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {

    private final PostImageServer postImageServer;

    @PostMapping("image/upload")
    public ResponseEntity<?> uploadBase64Image(@RequestBody ImageUploadRequest request) {
        try {
            String savedFileName = postImageServer.saveBase64PostImage(request.getBase64Image());
            return ResponseEntity.ok(CommonResponse.success(savedFileName));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 저장 중 오류가 발생했습니다.");
        }
    }
}
