package com.qrstudy.qrstudy.domain.entity.posttag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostTag is a Querydsl query type for PostTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostTag extends EntityPathBase<PostTag> {

    private static final long serialVersionUID = 50543148L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostTag postTag = new QPostTag("postTag");

    public final com.qrstudy.qrstudy.base.jpa.baseEntity.QBaseEntity _super = new com.qrstudy.qrstudy.base.jpa.baseEntity.QBaseEntity(this);

    public final com.qrstudy.qrstudy.domain.entity.member.QMember author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.qrstudy.qrstudy.domain.entity.post.QPost post;

    public final com.qrstudy.qrstudy.domain.entity.postkeyword.QPostKeyword postKeyword;

    public final NumberPath<Long> sortNo = createNumber("sortNo", Long.class);

    public QPostTag(String variable) {
        this(PostTag.class, forVariable(variable), INITS);
    }

    public QPostTag(Path<? extends PostTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostTag(PathMetadata metadata, PathInits inits) {
        this(PostTag.class, metadata, inits);
    }

    public QPostTag(Class<? extends PostTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.qrstudy.qrstudy.domain.entity.member.QMember(forProperty("author")) : null;
        this.post = inits.isInitialized("post") ? new com.qrstudy.qrstudy.domain.entity.post.QPost(forProperty("post"), inits.get("post")) : null;
        this.postKeyword = inits.isInitialized("postKeyword") ? new com.qrstudy.qrstudy.domain.entity.postkeyword.QPostKeyword(forProperty("postKeyword"), inits.get("postKeyword")) : null;
    }

}

