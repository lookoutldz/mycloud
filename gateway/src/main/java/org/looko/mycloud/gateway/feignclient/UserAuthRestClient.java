package org.looko.mycloud.gateway.feignclient;

import org.looko.mycloud.commonstarter.dto.UserAuthDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/feign/auth/user")
public interface UserAuthRestClient {
    @PostExchange("/usernameOrEmail")
    UserAuthDTO getUserByUsernameOrEmail(@RequestBody String usernameOrEmail);
}
