package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.exam.tomatoback.post.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByUserId(Long userId, Pageable pageable);

    @Query("""
    SELECT l.post FROM Like l
    WHERE l.user.id = :userId
    ORDER BY l.createdAt DESC
""")
    Page<Post> findLikedPostsOrderByLikedAt(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT l.post FROM Like l
    WHERE l.user.id = :userId
""")
    Page<Post> findLikedPosts(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT l.post FROM Like l
    WHERE l.user.id = :userId
    ORDER BY l.post.price DESC
""")
    Page<Post> findLikedPostsOrderByPrice(@Param("userId") Long userId, Pageable pageable);

    @Query(
            value = """
        SELECT p.*
        FROM posts p
        JOIN likes l1 ON p.id = l1.post_id
        WHERE l1.user_id = :userId
        GROUP BY p.id
        ORDER BY (
            SELECT COUNT(*) FROM likes l2 WHERE l2.post_id = p.id
        ) DESC
    """,
            countQuery = """
        SELECT COUNT(*) FROM (
            SELECT p.id
            FROM posts p
            JOIN likes l1 ON p.id = l1.post_id
            WHERE l1.user_id = :userId
            GROUP BY p.id
        ) AS counted
    """,
            nativeQuery = true
    )
    Page<Post> findLikedPostsOrderByPostLikeCountDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    JOIN PostProgress pp ON p.id = pp.post.id
    WHERE p.user.id = :userId
    AND pp.postStatus = 'SELLING'
    ORDER BY p.updatedAt desc
    
""")
    Page<Post> findSellingPostsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT p
    FROM Post p
    JOIN PostProgress pp ON p.id = pp.post.id
    WHERE pp.user.id = :userId
    AND pp.postStatus = 'END'
    ORDER BY p.updatedAt desc
    
""")
    Page<Post> findEndPostsByUserId(@Param("userId") Long userId, Pageable pageable);

    List<Post> findAllByDeletedFalse();
    Optional<Post> findByIdAndDeletedFalse(Long id);


    List<Post> findByUserIdAndStatus(Long userId, PostStatus status);
}
