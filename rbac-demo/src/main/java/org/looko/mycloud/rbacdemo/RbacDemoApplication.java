package org.looko.mycloud.rbacdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.looko.mycloud.rbacdemo.mapper")
@SpringBootApplication
public class RbacDemoApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(RbacDemoApplication.class, args);
    }

}
