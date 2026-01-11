package com.ourdressingtable.notification.component;

import com.ourdressingtable.notification.dto.NotificationEventPayload;
import com.ourdressingtable.notification.repository.ScheduledNotificationRepository;
import com.ourdressingtable.notification.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationConsumer {
    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final NotificationSenderService sender;

    @KafkaListener(topics = "notifications", groupId = "notification-sender",
                    containerFactory = "notificationKafkaListenerContainerFactory")
    public void listen(NotificationEventPayload notificationEventPayload) {
       Long notifyId = notificationEventPayload.getNotifyId();

       scheduledNotificationRepository.findById(notifyId).ifPresent(sender::deliver);
    }
}
