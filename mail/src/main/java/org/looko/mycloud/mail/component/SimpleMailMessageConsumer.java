package org.looko.mycloud.mail.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleMailMessageConsumer {

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    private final JavaMailSender javaMailSender;

    public SimpleMailMessageConsumer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送邮件逻辑
     * @param message message
     */
    @KafkaListener(topics = "${spring.kafka.custom.topic.email}")
    public void listen(SimpleMailMessage message) {
        try {
            message.setFrom(from);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件服务异常", e);
        }
    }

}
