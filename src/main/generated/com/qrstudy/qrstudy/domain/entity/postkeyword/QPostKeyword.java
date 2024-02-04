package com.qrstudy.qrstudy.domain.entity.postkeyword;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostKeyword is a Querydsl query type for PostKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostKeyword extends EntityPathBase<PostKeyword> {

    private static final long serialVersionUID = -704148406L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostKeyword postKeyword = new QPostKeyword("postKeyword");

    public final com.qrstudy.qrstudy.base.jpa.QBaseEntity _super = new com.qrstudy.qrstudy.base.jpa.QBaseEntity(this);

    public final com.qrstudy.qrstudy.domain.entity.member.QMember author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<<any>> postTags = createNumber("postTags", <any>.class);

    public final NumberPath<Long> total = createNumber("total", Long.class);

    public QPostKeyword(String variable) {
        this(PostKeyword.class, forVariable(variable), INITS);
    }

    public QPostKeyword(Path<? extends PostKeyword> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostKeyword(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostKeyword(PathMetadata metadata, PathInits inits) {
        this(PostKeyword.class, metadata, inits);
    }

    public QPostKeyword(Class<? extends PostKeyword> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.qrstudy.qrstudy.domain.entity.member.QMember(forProperty("author")) : null;
    }

}

