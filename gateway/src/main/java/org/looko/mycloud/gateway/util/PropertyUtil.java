package org.looko.mycloud.gateway.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PropertyUtil {

    public static String jwtSecret;

    @Value("${mycloud.gateway.jwt.secret}")
    private String secret;

    public static Long jwtExpireMillis;

    @Value("${mycloud.gateway.jwt.expire}")
    private String expire;

    @PostConstruct
    public void setJwtSecret() {
        jwtSecret = secret;
        jwtExpireMillis = Long.valueOf(expire);
    }
}
