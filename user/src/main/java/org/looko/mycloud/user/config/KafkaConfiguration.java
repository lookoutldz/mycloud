package org.looko.mycloud.user.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.custom.init.topic}")
    private String topic;

    @Bean
    public boolean topicInitializer(KafkaAdmin kafkaAdmin) {
        if (topic == null || topic.isEmpty()) {
            log.info("Kafka custom init topic not set, noting will be init.");
            return false;
        }
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> topicNames = topicsResult.names().get();
            for (String topicName : topicNames) {
                if (topicName.equals(topic)) {
                    log.info("Topic [" + topic + "] already exist, skip init.");
                    return true;
                }
            }
            NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
            CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
            if (createTopicsResult != null) {
                log.debug(createTopicsResult.toString());
            }
        } catch (Exception e) {
            log.error("Kafka topic init failed, email service may unusable!", e);
        }
        return true;
    }

}
