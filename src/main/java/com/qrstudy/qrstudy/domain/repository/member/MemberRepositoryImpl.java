package com.qrstudy.qrstudy.domain.repository.member;

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

import static com.qrstudy.qrstudy.domain.entity.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Member> findByKw(String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        switch (kwType) {
            case "username" -> builder.and(member.username.containsIgnoreCase(kw));
            case "nickname" -> builder.and(member.nickname.containsIgnoreCase(kw));
            case "email" -> builder.and(member.email.containsIgnoreCase(kw));
            default -> builder.and(
                    member.username.containsIgnoreCase(kw)
                            .or(member.nickname.containsIgnoreCase(kw))
                            .or(member.email.containsIgnoreCase(kw))
            );
        }

        JPAQuery<Member> membersQuery = jpaQueryFactory
                .selectDistinct(member)
                .from(member)
                .where(builder);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(member.getType(), member.getMetadata());
            membersQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        membersQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(builder);

        return PageableExecutionUtils.getPage(membersQuery.fetch(), pageable, totalQuery::fetchOne);
    }
}