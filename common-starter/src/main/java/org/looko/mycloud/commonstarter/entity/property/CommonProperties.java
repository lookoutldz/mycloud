package org.looko.mycloud.commonstarter.entity.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mycloud.common")
public class CommonProperties {
    private ControllerSubProperty controller;
}
