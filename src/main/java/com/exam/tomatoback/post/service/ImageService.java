package com.exam.tomatoback.post.service;

import com.exam.tomatoback.post.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

        private final ImageRepository imageRepository;
}
