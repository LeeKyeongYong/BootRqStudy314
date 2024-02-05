package com.qrstudy.qrstudy.domain.entity.article;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = 1572588100L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final com.qrstudy.qrstudy.base.jpa.baseEntity.QBaseEntity _super = new com.qrstudy.qrstudy.base.jpa.baseEntity.QBaseEntity(this);

    public final SetPath<com.qrstudy.qrstudy.domain.entity.articleTag.ArticleTag, com.qrstudy.qrstudy.domain.entity.articleTag.QArticleTag> articleTags = this.<com.qrstudy.qrstudy.domain.entity.articleTag.ArticleTag, com.qrstudy.qrstudy.domain.entity.articleTag.QArticleTag>createSet("articleTags", com.qrstudy.qrstudy.domain.entity.articleTag.ArticleTag.class, com.qrstudy.qrstudy.domain.entity.articleTag.QArticleTag.class, PathInits.DIRECT2);

    public final com.qrstudy.qrstudy.domain.entity.member.QMember author;

    public final com.qrstudy.qrstudy.domain.entity.board.QBoard board;

    public final StringPath body = createString("body");

    public final StringPath bodyHtml = createString("bodyHtml");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath subject = createString("subject");

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.qrstudy.qrstudy.domain.entity.member.QMember(forProperty("author")) : null;
        this.board = inits.isInitialized("board") ? new com.qrstudy.qrstudy.domain.entity.board.QBoard(forProperty("board")) : null;
    }

}

