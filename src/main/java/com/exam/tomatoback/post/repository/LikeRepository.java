package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Like;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository  extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
}