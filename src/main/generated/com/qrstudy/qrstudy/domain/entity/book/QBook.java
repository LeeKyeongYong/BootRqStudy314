package com.qrstudy.qrstudy.domain.entity.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = 1846819284L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final com.qrstudy.qrstudy.base.jpa.QBaseEntity _super = new com.qrstudy.qrstudy.base.jpa.QBaseEntity(this);

    public final com.qrstudy.qrstudy.domain.entity.member.QMember author;

    public final StringPath body = createString("body");

    public final StringPath bodyHtml = createString("bodyHtml");

    public final SetPath<com.qrstudy.qrstudy.domain.entity.bookTag.BookTag, com.qrstudy.qrstudy.domain.entity.bookTag.QBookTag> bookTags = this.<com.qrstudy.qrstudy.domain.entity.bookTag.BookTag, com.qrstudy.qrstudy.domain.entity.bookTag.QBookTag>createSet("bookTags", com.qrstudy.qrstudy.domain.entity.bookTag.BookTag.class, com.qrstudy.qrstudy.domain.entity.bookTag.QBookTag.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isPublic = createBoolean("isPublic");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.qrstudy.qrstudy.domain.entity.postkeyword.QPostKeyword postKeyword;

    public final StringPath subject = createString("subject");

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.qrstudy.qrstudy.domain.entity.member.QMember(forProperty("author")) : null;
        this.postKeyword = inits.isInitialized("postKeyword") ? new com.qrstudy.qrstudy.domain.entity.postkeyword.QPostKeyword(forProperty("postKeyword"), inits.get("postKeyword")) : null;
    }

}

