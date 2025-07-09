package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Image;
import com.exam.tomatoback.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> getAllByPost(Post post);
}
