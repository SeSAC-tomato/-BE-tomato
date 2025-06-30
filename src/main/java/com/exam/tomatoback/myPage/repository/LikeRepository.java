package com.exam.tomatoback.mypage.repository;

import com.exam.tomatoback.mypage.model.post.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

}
