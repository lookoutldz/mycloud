package org.looko.mycloud.user.feigncontroller;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.dto.UserAuthDTO;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.service.AuthorizationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/feign/auth")
@Validated
public class AuthFeignController {

    private final AuthorizationService authorizationService;

    public AuthFeignController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/user/usernameOrEmail")
    public UserAuthDTO getUserByUsernameOrEmail(@RequestBody String usernameOrEmail) {
        User user = authorizationService.getByUsernameOrEmail(usernameOrEmail);
        return user != null
                ? new UserAuthDTO(user.getId().toString(), user.getUsername(), user.getPassword())
                : null;
    }
}
