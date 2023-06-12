package org.looko.mycloud.mail.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleMailMessageConsumer {

    public static final String dLockPrefix = "distributionLock_";

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    private final JavaMailSender javaMailSender;

    private final RedissonClient redissonClient;

    public SimpleMailMessageConsumer(JavaMailSender javaMailSender, RedissonClient redissonClient) {
        this.javaMailSender = javaMailSender;
        this.redissonClient = redissonClient;
    }

    /**
     * 发送邮件逻辑
     * @param record ConsumerRecord
     */
    @KafkaListener(topics = "${spring.kafka.custom.topic.email}")
    public void handleSimpleEmailMessage(ConsumerRecord<String, SimpleMailMessage> record) {
        String key = record.key();
        RLock rLock = redissonClient.getLock(dLockPrefix + key);
        if (rLock.tryLock()) {
            String storeKey = genStoreKey(key);
            if (redissonClient.getBucket(storeKey).get() == key) {
                // 已被其它节点消费过
                return;
            }
            // 添加已消费标志
            redissonClient.getBucket(storeKey).set(key);
            SimpleMailMessage message = record.value();
            try {
                message.setFrom(from);
                javaMailSender.send(message);
            } catch (Exception e) {
                log.error("邮件服务异常", e);
                // 删除已消费标志
                redissonClient.getBucket(storeKey).delete();
            } finally {
                rLock.unlock();
            }
        }
    }

    private String genStoreKey(String baseKey) {
        return "mycloud:mail:mq_consumed:email:" + baseKey;
    }

}
