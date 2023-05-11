package org.looko.mycloud.emailstarter.autoconfigure.properties;

import lombok.Data;
import org.looko.mycloud.emailstarter.entity.EmailConfigEntity;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "mycloud.email")
public class EmailProperties {
    private final List<EmailConfigEntity> configs;
}
