package org.looko.mycloud.user.component;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
public class EmailManager {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.senderEmail}")
    private String senderEmail;
    @Value("${mail.senderPassword}")
    private String senderPassword;

    public void sendEmail(String subject, String content, String receiver) throws MessagingException, UnsupportedEncodingException {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.host", host);

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
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setSubject(subject);
        mimeMessage.setText(content);
        mimeMessage.setFrom(new InternetAddress(senderEmail, senderEmail, "UTF-8"));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

        Transport transport = session.getTransport();
        transport.connect(host, senderEmail, senderPassword);
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
    }
}
