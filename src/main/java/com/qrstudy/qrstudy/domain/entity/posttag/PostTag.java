package com.qrstudy.qrstudy.domain.entity.posttag;

import com.qrstudy.qrstudy.base.jpa.BaseEntity;
import com.qrstudy.qrstudy.domain.entity.member.Member;
import com.qrstudy.qrstudy.domain.entity.post.Post;
import com.qrstudy.qrstudy.domain.entity.postkeyword.PostKeyword;
import com.qrstudy.qrstudy.domain.standard.DocumentSortableKeyword;
import com.qrstudy.qrstudy.domain.standard.DocumentSortableTag;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder

@ToString(callSuper = true)
public class PostTag extends BaseEntity implements DocumentSortableTag {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Post post;

    @ManyToOne
    private PostKeyword postKeyword;

    private String content;

    @Setter(PRIVATE)
    private long sortNo;

    // 배경 시작
    // Post 엔티티의 postTags 필드에 @OneToMany 형태로 PostTag 들을 담고 있다.
    // PostKeyword 엔티티의 postTags 필드에 @OneToMany 형태로 PostTag 들을 담고 있다.
    // 즉 Post 와 PostKeyword 는 N:M 관계이다.
    // 그리고 그 중간 엔티티가 PostTag 이다.
    // Post 엔티티와 PostKeyword 엔티티는 postTags 필드의 타입으로 Set 을 사용한다.
    // @OrderBy 가 붙어 있기 때문에 실제 구현체는 LinkedHashSet 이다.
    // LinkedHashSet 이어야 하는 이유 : postTag 들이 중복이 없어야 하고 순서도 있어야 하기 때문
    // 중복의 조건 : 같은 author, 같은 post, 같은 content 를 가진 PostTag 는 중복이다.
    // 태그와 관련된 까다로운 작업(중복금지, 순서번호 세팅)이 많다. JPA 의 기능을 최대한 활용해서 SQL이 아닌 자바 로직으로 풀어보려고 한다.
    // 아래 두개의 함수를 오버라이드한 이유는 그것을 위한 배경 작업 이다.
    // 배경 끝
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostTag postTag = (PostTag) o;

        if (!Objects.equals(author, postTag.author)) return false;
        if (!Objects.equals(post, postTag.post)) return false;
        return Objects.equals(content, postTag.content);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public DocumentSortableKeyword _getKeyword() {
        return postKeyword;
    }

    @Override
    public void _setKeyword(DocumentSortableKeyword keyword) {
        this.postKeyword = (PostKeyword) keyword;
    }

    @Override
    public DocumentSortableTag _getDocumentSortableTag() {
        return this;
    }

    @Override
    public void _setSortNo(long sortNo) {
        setSortNo(sortNo);
    }
}