package com.qrstudy.qrstudy.domain.repository.post;

import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.post.Post;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import com.querydsl.core.BooleanBuilder;
import static com.qrstudy.qrstudy.domain.entity.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.isPublic.eq(isPublic));
        return findBy(kwType, kw, pageable, builder);
    }

    @Override
    public Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.author.eq(author));
        return findBy(kwType, kw, pageable, builder);
    }

    private Page<Post> findBy(String kwType, String kw, Pageable pageable, BooleanBuilder builder) {
        applyKeywordFilter(kwType, kw, builder);

        JPAQuery<Post> postsQuery = createPostsQuery(builder);
        applySorting(pageable, postsQuery);

        postsQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = createTotalQuery(builder);

        return PageableExecutionUtils.getPage(postsQuery.fetch(), pageable, totalQuery::fetchOne);
    }

    private void applyKeywordFilter(String kwType, String kw, BooleanBuilder builder) {
        switch (kwType) {
            case "subject" -> builder.and(post.subject.containsIgnoreCase(kw));
            case "body" -> builder.and(post.body.containsIgnoreCase(kw));
            default -> builder.and(
                    post.subject.containsIgnoreCase(kw)
                            .or(post.body.containsIgnoreCase(kw))
            );
        }
    }

    private JPAQuery<Post> createPostsQuery(BooleanBuilder builder) {
        return jpaQueryFactory
                .selectDistinct(post)
                .from(post)
                .where(builder);
    }

    private void applySorting(Pageable pageable, JPAQuery<Post> postsQuery) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            postsQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
    }

    private JPAQuery<Long> createTotalQuery(BooleanBuilder builder) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(builder);
    }
}