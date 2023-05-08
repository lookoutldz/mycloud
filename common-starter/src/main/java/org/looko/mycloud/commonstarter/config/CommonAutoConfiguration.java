package org.looko.mycloud.commonstarter.config;

import org.looko.mycloud.commonstarter.component.CommonDemo;
import org.looko.mycloud.commonstarter.entity.property.CommonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(value = {CommonDemo.class})
@EnableConfigurationProperties({CommonProperties.class})
public class CommonAutoConfiguration {

    @Bean(name = "commonDemo")
    public CommonDemo commonDemo(CommonProperties properties) {
        System.out.println("Config Loaded.");
        if (properties.getEnable()) {
            System.out.println("Common Module Enable.");
        }
        return new CommonDemo(properties.getEnable());
    }
}
