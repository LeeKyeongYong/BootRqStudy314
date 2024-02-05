package com.qrstudy.qrstudy.domain.entity.book;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import com.qrstudy.qrstudy.domain.entity.BookTag;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.postkeyword.PostKeyword;
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
public class Book extends BaseEntity implements DocumentHavingTags {
    @ManyToOne
    private PostKeyword postKeyword;

    @ManyToOne
    private Member author;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    private boolean isPublic;

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<BookTag> bookTags = new HashSet<>();

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public Set<? extends DocumentTag> _getTags() {
        return bookTags;
    }

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public DocumentTag _addTag(String tagContent) {
        BookTag tag = BookTag.builder()
                .author(author)
                .book(this)
                .content(tagContent)
                .build();

        bookTags.add(tag);

        return tag;
    }

    public String getPublicHanName() {
        return isPublic ? "공개" : "비공개";
    }

}