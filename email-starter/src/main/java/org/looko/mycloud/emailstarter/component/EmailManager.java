package org.looko.mycloud.emailstarter.component;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.emailstarter.entity.EmailConfigEntity;
import org.looko.mycloud.emailstarter.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static org.looko.mycloud.emailstarter.constant.EmailConfigText.*;
import static org.looko.mycloud.emailstarter.constant.ExceptionText.*;

@Slf4j
public class EmailManager {

    private final List<EmailConfigEntity> configs;

    public EmailManager(List<EmailConfigEntity> configs) {
        this.configs = configs;
    }

    public void sendEmail(String subject, String content, Collection<String> receivers) {
        sendEmail(subject, content, receivers, SendModeEnum.ONCE_PER_RECEIVER);
    }
    public void sendEmail(String subject, String content, Collection<String> receivers, SendModeEnum sendModeEnum) {
        if (configs == null || configs.isEmpty()) {
            throw new RuntimeException(EMAIL_CONFIG_WRONG);
        }
        if (receivers == null || !receivers.stream().allMatch(s -> StringUtils.emailPattern.matcher(s).matches())) {
            throw new RuntimeException(EMAIL_RECEIVER_WRONG);
        }

        switch (sendModeEnum) {
            case ONCE_PER_RECEIVER -> send(subject, content, receivers, configs.get(0));
            case ALL_PER_RECEIVER -> configs.forEach(config -> send(subject, content, receivers, config) );
            default -> throw new RuntimeException(EMAIL_STRATEGY_WRONG);
        }
    }

    private void send(String subject, String content, Collection<String> receivers, EmailConfigEntity config) {
        String host = config.host();
        String senderEmail = config.senderEmail();
        String senderPassword = config.senderPassword();
        String senderName = "MyCloudService-NoReply";

        Properties properties = new Properties();
        properties.setProperty(TRANSPORT_PROTOCOL, SMTP);
        properties.setProperty(SMTP_AUTH, TRUE);
        properties.setProperty(SMTP_SSL_ENABLE, TRUE);
        properties.setProperty(SMTP_SSL_PROTOCOLS, TLS_V1_2);
        properties.setProperty(SMTP_HOST, host);

        Session session = Session.getDefaultInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                }
        );
        session.setDebug(false);

        InternetAddress[] addresses = receivers
                .stream()
                .map(s -> {
                    try {
                        return new InternetAddress(s);
                    } catch (AddressException e) {
                        log.error("收件人地址转换失败：" + s, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList()
                .toArray(new InternetAddress[0]);

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);
            mimeMessage.setFrom(new InternetAddress(senderEmail, senderName, UTF8));
            mimeMessage.setRecipients(Message.RecipientType.TO, addresses);

            Transport transport = session.getTransport();
            transport.connect(host, senderEmail, senderPassword);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            log.info("Email sent from [{}] to [{}], Subject [{}]", senderEmail, addresses, subject);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // todo load balance strategy
    enum SendModeEnum {
        ONCE_PER_RECEIVER,
        ALL_PER_RECEIVER; // normally for test
    }
}
