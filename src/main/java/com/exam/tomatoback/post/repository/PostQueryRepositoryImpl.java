package com.exam.tomatoback.post.repository;

import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.model.PostStatus;
import com.exam.tomatoback.post.model.QPost;
import com.exam.tomatoback.post.model.QPostProgress;
import com.exam.tomatoback.web.dto.post.post.PostPageRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.exam.tomatoback.post.model.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository{

    private final JPAQueryFactory queryFactory;
    private final QPostProgress postProgress = QPostProgress.postProgress;

    @Override
    public Page<Post> searchWithFiltersDeleteFalse(PostPageRequest request, Pageable pageable){
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(post.deleted.eq(false));
        if (StringUtils.hasText(request.getKeyword())) {
            builder.and(
                    post.title.containsIgnoreCase(request.getKeyword()) // containsIgnoreCase: 대소문자 구분 없이 포함 여부 확인
                            .or(post.content.containsIgnoreCase(request.getKeyword()))
            );
        }
        if (request.getProductCategory() != null) {
            builder.and(post.productCategory.eq(request.getProductCategory()));
        }
//        if (request.getRegion() != null && !request.getRegion().isEmpty()) {
//            builder.and(post.region.eq(request.getRegion()));
//        }
        if (Boolean.TRUE.equals(request.getSelling())) {
            builder.and(postProgress.postStatus.eq(PostStatus.SELLING));
        }
        if (request.getMinPrice() != null) {
            builder.and(post.price.goe(request.getMinPrice()));
        }
        if (request.getMaxPrice() != null) {
            builder.and(post.price.loe(request.getMaxPrice()));
        }

        JPAQuery<Post> query = queryFactory.selectFrom(post).where(builder);
        long total = query.fetch().size();


        List<Post> contents = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.updatedAt.desc())
                .fetch();

        return new PageImpl<>(contents, pageable, total);
    }
}
