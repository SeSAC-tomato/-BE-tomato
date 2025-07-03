package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.PostCategory;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.ProductCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
        private String title;
        private Integer price;
        private String content;
        private PostStatus postStatus;
        private PostCategory postCategory;
        private Boolean deleted;
        private Long userId;

        public Post toDomain() {
            Post post = Post.builder()
                    .title(this.title)
                    .price(this.price)
                    .content(this.content)
                    .postStatus(this.postStatus)
                    .postCategory(this.postCategory)
                    .deleted(this.deleted)
                    .build();

            User user = User.builder()
                    .id(this.userId)
                    .build();

            post.setUser(user);
            return post;
        }
}
