package org.looko.mycloud.user.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class EmailManager {

    private final KafkaTemplate<String, SimpleMailMessage> kafkaTemplate;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    @Value("${spring.kafka.custom.init.topic}")
    private String topic;

    public EmailManager(KafkaTemplate<String, SimpleMailMessage> kafkaTemplate, JavaMailSender javaMailSender) {
        this.kafkaTemplate = kafkaTemplate;
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发到 MQ, 解耦
         try {
             pushToMQ(message);
         } catch (Exception e) {
             log.error("邮件发送异常", e);
             // TODO 兜底策略
         }
    }

    public void pushToMQ(SimpleMailMessage message) {
        ProducerRecord<String, SimpleMailMessage> record = new ProducerRecord<>(topic, "key", message);
        CompletableFuture<SendResult<String, SimpleMailMessage>> future = kafkaTemplate.send(record);
        future.thenApply((sendResult) -> {
            RecordMetadata metadata = sendResult.getRecordMetadata();
            if (metadata.hasOffset()) {
                log.info("Message has send to kafka, key: {}, data: {}", "key", message.toString());
                log.info("Topic: {}, partition: {}, offset: {}.", metadata.topic(), metadata.partition(), metadata.offset());
            }
            return true;
        });
    }

    /**
     * 真正的发送邮件逻辑
     * @param message message
     */
    @KafkaListener(topics = "${spring.kafka.custom.init.topic}")
    public void listen(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送异常", e);
            // TODO 兜底策略
        }

    }

}
