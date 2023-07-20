package org.looko.mycloud.commonstarter.entity.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mycloud")
public class CommonProperties {
    private Boolean globalControllerResponseWrapping;
}
