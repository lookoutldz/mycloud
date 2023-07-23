package org.looko.mycloud.gateway.config;

import org.looko.mycloud.gateway.feignclient.UserAuthRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfiguration {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Bean
    public UserAuthRestClient UserAuthRestClient() {
        WebClient client = webClientBuilder.baseUrl("http://cloud-user").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return factory.createClient(UserAuthRestClient.class);
    }

}
