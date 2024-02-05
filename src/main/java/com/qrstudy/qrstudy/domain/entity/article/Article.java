package com.qrstudy.qrstudy.domain.entity.article;

import com.qrstudy.qrstudy.base.jpa.baseEntity.BaseEntity;
import com.qrstudy.qrstudy.domain.entity.articleTag.ArticleTag;
import com.qrstudy.qrstudy.domain.entity.board.Board;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.standard.repository.DocumentHavingTags;
import com.qrstudy.qrstudy.domain.standard.repository.DocumentTag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity implements DocumentHavingTags {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Board board;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    @OneToMany(mappedBy = "article", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<ArticleTag> articleTags = new HashSet<>();

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public Set<? extends DocumentTag> _getTags() {
        return articleTags;
    }

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public DocumentTag _addTag(String tagContent) {
        ArticleTag tag = ArticleTag.builder()
                .author(author)
                .article(this)
                .content(tagContent)
                .build();

        articleTags.add(tag);

        return tag;
    }
}