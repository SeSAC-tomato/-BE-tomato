package com.exam.tomatoback.like.repository;

import com.exam.tomatoback.like.model.Like;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Like> findByPostAndUser(Post post, User user);
}

