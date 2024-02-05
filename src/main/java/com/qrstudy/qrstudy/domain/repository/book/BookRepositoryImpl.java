package com.qrstudy.qrstudy.domain.repository.book;

import com.qrstudy.qrstudy.domain.entity.member.Member;
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
import java.awt.print.Book;
import static com.qrstudy.qrstudy.domain.entity.book.QBook.book;
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Book> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(book.isPublic.eq(isPublic));
        return findBy(kwType, kw, pageable, builder);
    }

    @Override
    public Page<Book> findByKw(Member author, String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(book.author.eq(author));
        return findBy(kwType, kw, pageable, builder);
    }

    private Page<Book> findBy(String kwType, String kw, Pageable pageable, BooleanBuilder builder) {
        applyKeywordFilter(kwType, kw, builder);

        JPAQuery<Book> booksQuery = createBooksQuery(builder);
        applySorting(pageable, booksQuery);

        booksQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = createTotalQuery(builder);

        return PageableExecutionUtils.getPage(booksQuery.fetch(), pageable, totalQuery::fetchOne);
    }

    private void applyKeywordFilter(String kwType, String kw, BooleanBuilder builder) {
        switch (kwType) {
            case "subject" -> builder.and(book.subject.containsIgnoreCase(kw));
            case "body" -> builder.and(book.body.containsIgnoreCase(kw));
            default -> builder.and(
                    book.subject.containsIgnoreCase(kw)
                            .or(book.body.containsIgnoreCase(kw))
            );
        }
    }

    private JPAQuery<Book> createBooksQuery(BooleanBuilder builder) {
        return jpaQueryFactory
                .selectDistinct(book)
                .from(book)
                .where(builder);
    }

    private void applySorting(Pageable pageable, JPAQuery<Book> booksQuery) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(book.getType(), book.getMetadata());
            booksQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
    }

    private JPAQuery<Long> createTotalQuery(BooleanBuilder builder) {
        return jpaQueryFactory
                .select(book.count())
                .from(book)
                .where(builder);
    }
}