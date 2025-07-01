package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Categories;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
        private String title;
        private int price;
        private String content;
        private String status;
        private boolean deleted;
        private Categories category;
        private Long userId;

        public Post toDomain() {
            Post post = Post.builder()
                    .title(this.title)
                    .price(this.price)
                    .content(this.content)
                    .deleted(this.deleted)
                    .category(this.category)
                    .build();

            User user = User.builder()
                    .id(this.userId)
                    .build();

            post.setUser(user);
            return post;
        }
}
