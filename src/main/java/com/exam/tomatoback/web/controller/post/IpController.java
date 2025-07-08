package com.exam.tomatoback.web.controller.post;

import com.exam.tomatoback.infrastructure.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class IpController {

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getPostImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(Constants.POST_IMAGE_DIR).resolve(filename).normalize();
            Resource postResource = new UrlResource(filePath.toUri());

            if (!postResource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 간단하게 이미지로 한정 (jpg/png/gif 등)
            String contentType = "application/octet-stream";
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.endsWith(".gif")) {
                contentType = "image/gif";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + postResource.getFilename() + "\"")
                    .body(postResource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

