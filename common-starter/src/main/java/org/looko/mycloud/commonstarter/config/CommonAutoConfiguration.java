package org.looko.mycloud.commonstarter.config;

import org.looko.mycloud.commonstarter.aop.GlobalControllerAdvice;
import org.looko.mycloud.commonstarter.component.CommonPropertyCheck;
import org.looko.mycloud.commonstarter.entity.property.CommonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(value = {CommonPropertyCheck.class})
@EnableConfigurationProperties({CommonProperties.class})
public class CommonAutoConfiguration {

    @Bean(initMethod = "logProperty")
    public CommonPropertyCheck commonPropertyCheck(CommonProperties props) {
        return new CommonPropertyCheck(props);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mycloud", name = "globalControllerResponseWrapping", havingValue = "true")
    public GlobalControllerAdvice globalControllerAdvice() {
        return new GlobalControllerAdvice();
    }
}
