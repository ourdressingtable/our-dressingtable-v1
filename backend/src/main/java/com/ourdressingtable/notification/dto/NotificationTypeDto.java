package com.ourdressingtable.notification.dto;

import com.ourdressingtable.notification.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NotificationTypeDto {

    private final String code;
    private final String displayName;
    private final String icon;

    @Builder
    public NotificationTypeDto(String code, String displayName, String icon) {
        this.code = code;
        this.displayName = displayName;
        this.icon = icon;
    }

    public static NotificationTypeDto from(NotificationType type) {
        return switch (type) {
            case COSMETIC_EXPIRY_MONTH_BEFORE ->
                    new NotificationTypeDto(type.name(), "한 달 전 알림","calendar-clock");
            case COSMETIC_EXPIRY_WEEK_BEFORE ->
                    new NotificationTypeDto(type.name(), "일주일 전 알림","calendar-week");
            case COSMETIC_EXPIRY_DAY_BEFORE ->
                    new NotificationTypeDto(type.name(), "하루 전 알림","clock");
            case COSMETIC_EXPIRY_DAY_OF ->
                    new NotificationTypeDto(type.name(), "당일 알림","alert-circle");
        };
    }
}
