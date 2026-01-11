package com.ourdressingtable.notification.dto;

import com.ourdressingtable.notification.domain.NotificationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEventPayload {

    private Long notifyId;
    private Long memberId;
    private NotificationType type;
    private String payloadJson;

    @Builder
    public NotificationEventPayload(Long notifyId, Long memberId, String type, String payloadJson) {
        this.notifyId = notifyId;
        this.memberId = memberId;
        this.type = NotificationType.valueOf(type);
        this.payloadJson = payloadJson;
    }
}
