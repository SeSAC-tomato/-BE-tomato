package com.exam.tomatoback.web.dto.post;

import com.exam.tomatoback.post.model.Category;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.Status;
import com.exam.tomatoback.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {
        private String title;
        private int price;
        private String content;
        private Status status;
        private Category category;
        private boolean deleted;
        private Long userId;
        private List<ImageResponse> images;

        public Post toDomain() {
            Post post = Post.builder()
                    .title(this.title)
                    .price(this.price)
                    .content(this.content)
                    .status(this.status)
                    .category(this.category)
                    .deleted(this.deleted)
                    .build();

            User user = User.builder()
                    .id(this.userId)
                    .build();

            post.setUser(user);
            return post;
        }
}
