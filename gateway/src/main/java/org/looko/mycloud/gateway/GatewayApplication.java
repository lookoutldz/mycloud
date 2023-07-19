package org.looko.mycloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		System.setProperty("nacos.logging.default.config.enabled","false");
		SpringApplication.run(GatewayApplication.class, args);
	}

}
