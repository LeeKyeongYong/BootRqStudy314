package com.qrstudy.qrstudy.domain.repository.article;

import com.qrstudy.qrstudy.domain.entity.article.Article;
import com.qrstudy.qrstudy.domain.entity.board.Board;
import com.querydsl.core.BooleanBuilder;
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

import static com.qrstudy.qrstudy.domain.entity.article.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Article> findByKw(Board board, String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(article.board.eq(board));

        switch (kwType) {
            case "subject" -> builder.and(article.subject.containsIgnoreCase(kw));
            case "body" -> builder.and(article.body.containsIgnoreCase(kw));
            case "nickname" -> builder.and(article.author.nickname.containsIgnoreCase(kw));
            default -> builder.and(
                    article.subject.containsIgnoreCase(kw)
                            .or(article.body.containsIgnoreCase(kw))
                            .or(article.author.nickname.containsIgnoreCase(kw))
            );
        }

        JPAQuery<Article> articlesQuery = jpaQueryFactory
                .selectDistinct(article)
                .from(article)
                .where(builder);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(article.getType(), article.getMetadata());
            articlesQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        articlesQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(article.count())
                .from(article)
                .where(builder);

        return PageableExecutionUtils.getPage(articlesQuery.fetch(), pageable, totalQuery::fetchOne);
    }
}