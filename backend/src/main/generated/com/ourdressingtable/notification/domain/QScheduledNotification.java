package com.ourdressingtable.notification.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScheduledNotification is a Querydsl query type for ScheduledNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduledNotification extends EntityPathBase<ScheduledNotification> {

    private static final long serialVersionUID = 474761863L;

    public static final QScheduledNotification scheduledNotification = new QScheduledNotification("scheduledNotification");

    public final com.ourdressingtable.common.util.QBaseTimeEntity _super = new com.ourdressingtable.common.util.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final DateTimePath<java.time.Instant> notifyAt = createDateTime("notifyAt", java.time.Instant.class);

    public final StringPath payloadJson = createString("payloadJson");

    public final EnumPath<NotificationStatus> status = createEnum("status", NotificationStatus.class);

    public final StringPath timezone = createString("timezone");

    public final EnumPath<NotificationType> type = createEnum("type", NotificationType.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QScheduledNotification(String variable) {
        super(ScheduledNotification.class, forVariable(variable));
    }

    public QScheduledNotification(Path<? extends ScheduledNotification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScheduledNotification(PathMetadata metadata) {
        super(ScheduledNotification.class, metadata);
    }

}

