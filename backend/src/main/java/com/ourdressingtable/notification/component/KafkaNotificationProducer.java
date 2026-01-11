package com.ourdressingtable.notification.component;

import com.ourdressingtable.notification.dto.NotificationEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {
    private final KafkaTemplate<String, NotificationEventPayload> kafkaTemplate;
    public void send(NotificationEventPayload notificationEventPayload) {
        String key = String.valueOf(notificationEventPayload.getMemberId());
        kafkaTemplate.send("notification", key, notificationEventPayload);
    }
}
