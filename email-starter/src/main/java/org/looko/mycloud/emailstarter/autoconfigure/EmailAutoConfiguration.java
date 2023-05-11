package org.looko.mycloud.emailstarter.autoconfigure;

import org.looko.mycloud.emailstarter.autoconfigure.properties.EmailProperties;
import org.looko.mycloud.emailstarter.component.EmailManager;
import org.looko.mycloud.emailstarter.entity.EmailConfigEntity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.looko.mycloud.emailstarter.constant.ExceptionText.*;
import static org.looko.mycloud.emailstarter.util.StringUtils.emailPattern;
import static org.looko.mycloud.emailstarter.util.StringUtils.lengthPattern1to128;

@Configuration
@EnableConfigurationProperties(value = {EmailProperties.class})
public class EmailAutoConfiguration {

    @Bean
    public EmailManager emailManager(EmailProperties properties) {
        // validation
        List<EmailConfigEntity> configs = properties.getConfigs();
        if (configs == null || configs.isEmpty()) {
            throw new RuntimeException(EMAIL_CONFIG_WRONG);
        }
        for (EmailConfigEntity config : configs) {
            if (!lengthPattern1to128.matcher(config.host()).matches()
                    || !lengthPattern1to128.matcher(config.senderEmail()).matches()
                    || !lengthPattern1to128.matcher(config.senderPassword()).matches()) {
                throw new RuntimeException(EMAIL_CONFIG_LENGTH_WRONG);
            }
            if (!emailPattern.matcher(config.senderEmail()).matches()) {
                throw new RuntimeException(EMAIL_CONFIG_SENDER_WRONG);
            }
        }
        // bean
        return new EmailManager(configs);
    }
}
