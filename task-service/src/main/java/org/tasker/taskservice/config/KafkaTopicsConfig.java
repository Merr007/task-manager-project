package org.tasker.taskservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic taskEventsTopic(@Value("${TASK_EVENTS_TOPIC:task.events.v1}") String topic) {
        // 3 partitions by default, adjust per load
        return new NewTopic(topic, 3, (short) 1);
    }
}
