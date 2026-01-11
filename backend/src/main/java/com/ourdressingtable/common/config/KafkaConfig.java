package com.ourdressingtable.common.config;

import com.ourdressingtable.chat.dto.ChatMessageRequest;
import java.util.HashMap;
import java.util.Map;

import com.ourdressingtable.notification.dto.NotificationEventPayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    // 공통
    private Map<String, Object> commonProducerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    // 채팅용
    @Bean
    public ProducerFactory<String, ChatMessageRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(commonProducerConfig());
    }

    @Bean(name = "chatKafkaTemplate")
    public KafkaTemplate<String, ChatMessageRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public ConsumerFactory<String, ChatMessageRequest> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-consumer");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<ChatMessageRequest> deserializer = new JsonDeserializer<>(ChatMessageRequest.class);
        deserializer.addTrustedPackages("com.ourdressingtable.*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 알림용
    @Bean
    public ProducerFactory<String, NotificationEventPayload> notificationProducerFactory() {
        Map<String, Object> config = commonProducerConfig();
        // 타입 헤더 포함
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        // 신뢰 패키지 지정
        config.put(JsonSerializer.TYPE_MAPPINGS,
                "notifyPayload:com.ourdressingtable.chat.dto.NotificationEventPayload");
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String, NotificationEventPayload> notificationKafkaTemplate() {
        return new KafkaTemplate<>(notificationProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, NotificationEventPayload> notificationConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-sender");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<NotificationEventPayload> deserializer = new JsonDeserializer<>(NotificationEventPayload.class);
        deserializer.addTrustedPackages("com.ourdressingtable.*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean(name = "notificationKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, NotificationEventPayload>  notificationListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationEventPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(notificationConsumerFactory());
        return factory;
    }

}
