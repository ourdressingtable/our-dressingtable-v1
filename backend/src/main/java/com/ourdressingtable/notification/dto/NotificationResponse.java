package com.ourdressingtable.notification.dto;

import com.ourdressingtable.notification.domain.NotificationType;
import com.ourdressingtable.notification.domain.ScheduledNotification;
import com.ourdressingtable.notification.inbox.NotificationInbox;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationResponse {

    private String id;
    private Long memberId;
    private NotificationType type;
    private String title;
    private String body;
    private String dataJson;
    private boolean read;
    private Instant createdAt;

    @Builder
    public NotificationResponse(String id, Long memberId, NotificationType type, String title, String body,
                                String dataJson, boolean read, Instant createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.dataJson = dataJson;
        this.read = read;
        this.createdAt = createdAt;
    }

    public static NotificationResponse from(NotificationInbox inbox) {
        return NotificationResponse.builder()
                .id(inbox.getId() != null ? inbox.getId().toHexString() : null)
                .memberId(inbox.getMemberId())
                .type(NotificationType.valueOf(inbox.getType()))
                .title(inbox.getTitle())
                .body(inbox.getBody())
                .dataJson(inbox.getDataJson())
                .read(inbox.isRead())
                .createdAt(inbox.getCreatedAt())
                .build();
    }

    public static  NotificationResponse from(ScheduledNotification scheduledNotification, String title, String body) {
        return NotificationResponse.builder()
                .id(String.valueOf(scheduledNotification.getId()))
                .memberId(scheduledNotification.getMemberId())
                .type(scheduledNotification.getType())
                .title(title)
                .body(body)
                .dataJson(scheduledNotification.getPayloadJson())
                .read(false)
                .createdAt(scheduledNotification.getCreatedAt())
                .build();
    }
}
