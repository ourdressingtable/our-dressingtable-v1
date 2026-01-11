package com.ourdressingtable.notification.dto;

import com.ourdressingtable.notification.domain.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationCreateRequest {

    private Long memberId;
    private NotificationType type;
    private String title;
    private String body;
    private String dataJson;

    private LocalDateTime notifyAt;
    private String timezone = "Asia/Seoul";

    public Instant toUtcInstant() {
        ZonedDateTime kst = notifyAt.atZone(ZoneId.of(timezone));
        return kst.toInstant();
    }

    @Builder
    public NotificationCreateRequest(Long memberId, String title, String body, String type, String dataJson, LocalDateTime notifyAt, String timezone) {
        this.memberId = memberId;
        this.title = title;
        this.body = body;
        this.type = NotificationType.valueOf(type);
        this.dataJson = dataJson;
        this.notifyAt = notifyAt;
    }

}
