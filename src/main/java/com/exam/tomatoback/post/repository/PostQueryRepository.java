package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.web.dto.post.post.PostPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryRepository {
    Page<Post> searchWithFiltersDeleteFalse(PostPageRequest request, Pageable pageable);
}
