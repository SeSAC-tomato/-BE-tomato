package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostProgressRepository extends JpaRepository<PostProgress, Long> {

}
