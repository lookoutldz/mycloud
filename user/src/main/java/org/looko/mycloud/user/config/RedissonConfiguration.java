package org.looko.mycloud.user.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfiguration {

    @Bean
    public RedissonClient redissonClient() throws IOException {
        // use "rediss://" for SSL connection
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName("mymaster")
                .addSentinelAddress(
                        "redis://archlinux:26379",
                        "redis://archlinux:26380",
                        "redis://archlinux:26381"
                )
                .setPassword("root")
                .setMasterConnectionPoolSize(2)
                .setMasterConnectionMinimumIdleSize(1)
                .setSlaveConnectionPoolSize(4)
                .setSlaveConnectionMinimumIdleSize(2)
                .setDatabase(0);
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
