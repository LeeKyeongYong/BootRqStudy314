package com.qrstudy.qrstudy.domain.entity.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSendEmailLog is a Querydsl query type for SendEmailLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSendEmailLog extends EntityPathBase<SendEmailLog> {

    private static final long serialVersionUID = -737425732L;

    public static final QSendEmailLog sendEmailLog = new QSendEmailLog("sendEmailLog");

    public final com.qrstudy.qrstudy.base.jpa.QBaseEntity _super = new com.qrstudy.qrstudy.base.jpa.QBaseEntity(this);

    public final StringPath body = createString("body");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> failDate = createDateTime("failDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath message = createString("message");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath resultCode = createString("resultCode");

    public final DateTimePath<java.time.LocalDateTime> sendEndDate = createDateTime("sendEndDate", java.time.LocalDateTime.class);

    public final StringPath subject = createString("subject");

    public QSendEmailLog(String variable) {
        super(SendEmailLog.class, forVariable(variable));
    }

    public QSendEmailLog(Path<? extends SendEmailLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSendEmailLog(PathMetadata metadata) {
        super(SendEmailLog.class, metadata);
    }

}

