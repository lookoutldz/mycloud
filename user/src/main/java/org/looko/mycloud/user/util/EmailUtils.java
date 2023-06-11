package org.looko.mycloud.user.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.exception.MyEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static org.looko.mycloud.user.constant.Parameters.validcodeExpireMinutes;

@Slf4j
@Component
public class EmailUtils {
    private final KafkaTemplate<String, SimpleMailMessage> kafkaTemplate;

    @Value("${spring.kafka.custom.topic.email}")
    private String topic;

    public EmailUtils(KafkaTemplate<String, SimpleMailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 发布邮件到消息队列
     * @param businessTypeEnum 业务类型
     * @param to 收件人邮箱
     * @param validcode 验证码
     */
    public void publishValidcodeEmail(BusinessTypeEnum businessTypeEnum, String to, String validcode) {
        final String key = "SimpleMailMessage";
        SimpleMailMessage message = genValidcodeEmail(businessTypeEnum, to, validcode);
        ProducerRecord<String, SimpleMailMessage> record = new ProducerRecord<>(topic, key, message);
        CompletableFuture<SendResult<String, SimpleMailMessage>> future = kafkaTemplate.send(record);
        future.thenApply((sendResult) -> {
            RecordMetadata metadata = sendResult.getRecordMetadata();
            if (metadata.hasOffset()) {
                log.info("Message has send to kafka, key: {}, data: {}", key, message);
                log.info("Topic: {}, partition: {}, offset: {}.", metadata.topic(), metadata.partition(), metadata.offset());
            }
            return true;
        });
    }

    /**
     * 邮件生成逻辑
     * @param businessTypeEnum 业务类型
     * @param to 发件人邮箱地址
     * @param validcode 验证码
     * @return SimpleMailMessage
     */
    private SimpleMailMessage genValidcodeEmail(BusinessTypeEnum businessTypeEnum, String to, String validcode) throws MyEmailException {
        String subject;
        String content;
        switch (businessTypeEnum) {
            case VALIDCODE_LOGIN -> {
                subject = "登录 MyCloud";
                content = "您正在进行登录 MyCloud 的操作, 验证码为 " + validcode + " , " + validcodeExpireMinutes
                        + " 分钟内有效。\n若非您的操作请忽略此封邮件。\n\tMyCloud 团队";
            }
            case VALIDCODE_REGISTER -> {
                subject = "注册 MyCloud";
                content = "您正在进行注册 MyCloud 账户的操作, 验证码为 " + validcode + " , " + validcodeExpireMinutes
                        + " 分钟内有效。\n若非您的操作请忽略此封邮件。\n\tMyCloud 团队";
            }
            case VALIDCODE_RESET_PASSWORD -> {
                subject = "重置 MyCloud 密码";
                content = "您正在进行 MyCloud 账户的重置密码操作, 验证码为 " + validcode + " , " + validcodeExpireMinutes
                        + " 分钟内有效。\n若非您的操作请忽略此封邮件。\n\tMyCloud 团队";
            }
            default -> throw new MyEmailException(businessTypeEnum.name() + "类型业务尚无邮件模板。");
        }
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);

        return message;
    }
}
