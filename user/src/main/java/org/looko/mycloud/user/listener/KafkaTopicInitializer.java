package org.looko.mycloud.user.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.looko.mycloud.user.constant.Parameters;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class KafkaTopicInitializer implements ApplicationRunner {

    @Value("${spring.kafka.custom.topic.email}")
    private String topic;
    private final KafkaAdmin kafkaAdmin;
    private final RedissonClient redissonClient;

    public KafkaTopicInitializer(RedissonClient redissonClient, KafkaAdmin kafkaAdmin) {
        this.kafkaAdmin = kafkaAdmin;
        this.redissonClient = redissonClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        RLock rLock = redissonClient.getLock(Parameters.dLockPrefix + topic);
        if (rLock.tryLock()) {
            try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
                ListTopicsResult topicsResult = adminClient.listTopics();
                Set<String> topicNames = topicsResult.names().get();
                for (String topicName : topicNames) {
                    if (topicName.equals(topic)) {
                        log.info("Kafka Topic[{}] 已存在, 无需初始化.", topic);
                        return;
                    }
                }
                NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
                CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
                if (createTopicsResult != null) {
                    log.debug(createTopicsResult.toString());
                    log.info("Kafka Topic[{}] 初始化成功.", topic);
                }
            } catch (Exception e) {
                log.error("Kafka Topic[{}] 初始化失败.", topic, e);
            } finally {
                rLock.unlock();
            }
        }
    }
}
