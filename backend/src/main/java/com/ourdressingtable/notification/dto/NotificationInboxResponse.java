package com.ourdressingtable.notification.dto;

import com.ourdressingtable.notification.inbox.NotificationInbox;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationInboxResponse {

    private String id;
    private Long notificationId;
    private String title;
    private String body;
    private String type;
    private String dataJson;
    private boolean read;
    private String createdAt;

    @Builder
    public NotificationInboxResponse(String id, Long notificationId, String title, String body, String type, String dataJson, boolean read, String createdAt) {
        this.id = id;
        this.notificationId = notificationId;
        this.title = title;
        this.body = body;
        this.type = type;
        this.dataJson = dataJson;
        this.read = read;
        this.createdAt = createdAt;
    }

    public static NotificationInboxResponse from(NotificationInbox notificationInbox) {
        return NotificationInboxResponse.builder()
                .id(notificationInbox.getId() != null ? notificationInbox.getId().toHexString() : null)
                .notificationId(notificationInbox.getNotificationId())
                .title(notificationInbox.getTitle())
                .body(notificationInbox.getBody())
                .type(notificationInbox.getType())
                .dataJson(notificationInbox.getDataJson())
                .read(notificationInbox.isRead())
                .createdAt(notificationInbox.getCreatedAt() != null ? notificationInbox.getCreatedAt().toString() : null)
                .build();
    }
}
