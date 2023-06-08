package org.looko.mycloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.service.UserService;
import org.looko.mycloud.user.util.EmailUtils;
import org.looko.mycloud.user.util.ValidcodeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.looko.mycloud.user.enumeration.BusinessTypeEnum.VALIDCODE_REGISTER;
import static org.looko.mycloud.user.enumeration.BusinessTypeEnum.VALIDCODE_RESET_PASSWORD;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final EmailUtils emailUtils;
    private final ValidcodeUtils validcodeUtils;

    public AuthController(EmailUtils emailUtils, UserService userService, ValidcodeUtils validcodeUtils) {
        this.userService = userService;
        this.emailUtils = emailUtils;
        this.validcodeUtils = validcodeUtils;
    }

    @PostMapping("/validcode/register/{email}")
    public ResponseEntity<String> sendRegisterCode(@PathVariable String email) {

        String validcode = validcodeUtils.genValidcode();
        validcodeUtils.saveValidcode(VALIDCODE_REGISTER, email, validcode);

        emailUtils.postValidcodeEmail(VALIDCODE_REGISTER, email, validcode);
        return ResponseEntity.success("验证码邮件已发送，请注意查收");
    }

    @PostMapping("/validcode/resetPassword/{email}")
    public ResponseEntity<String> sendResetPasswordCode(@PathVariable String email) {
        String validcode = validcodeUtils.genValidcode();
        validcodeUtils.saveValidcode(VALIDCODE_RESET_PASSWORD, email, validcode);
        emailUtils.postValidcodeEmail(VALIDCODE_RESET_PASSWORD, email, validcode);
        return ResponseEntity.success("验证码邮件已发送，请注意查收");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String validcode = data.get("validcode");
        String email = data.get("email");
        if (username == null || password == null || validcode == null || email == null) {
            throw new RuntimeException("注册信息不完整");
        }
        User user = userService.register(username, password, email, validcode);
        log.info("用户注册成功：id:" + user.getId() + ",username:" + user.getUsername() + ",email:" + user.getEmail());
        return ResponseEntity.success("注册成功");
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email, @RequestParam Map<String, String> data) {
        String password = data.get("password");
        String validcode = data.get("validcode");
        if (password == null || validcode == null) {
            throw new RuntimeException("密码或验证码为空");
        }
        userService.resetPassword(email, password, validcode);
        log.info("用户重置密码成功：email:" + email);
        return ResponseEntity.success("密码重置成功");
    }
}
