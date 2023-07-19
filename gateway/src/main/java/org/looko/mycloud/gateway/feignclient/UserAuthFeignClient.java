package org.looko.mycloud.gateway.feignclient;

import org.looko.mycloud.commonstarter.dto.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cloud-user")
public interface UserAuthFeignClient {
    @PostMapping("/feign/auth/user/usernameOrEmail")
    UserAuthDTO getUserByUsernameOrEmail(@RequestBody String usernameOrEmail);
}
