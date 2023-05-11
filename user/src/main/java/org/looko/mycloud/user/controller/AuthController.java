package org.looko.mycloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.emailstarter.component.EmailManager;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.service.TbValidcodeService;
import org.looko.mycloud.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmailManager emailManager;

    private final UserService userService;

    private final TbValidcodeService tbValidcodeService;

    public AuthController(EmailManager emailManager, UserService userService, TbValidcodeService tbValidcodeService) {
        this.emailManager = emailManager;
        this.userService = userService;
        this.tbValidcodeService = tbValidcodeService;
    }

    @PostMapping("/validcode/signup/{email}")
    public ResponseEntity<String> sendSignupCode(@PathVariable String email) {
        int randomNumber = new Random().nextInt(9999 - 1000 + 1) + 1000;
        String subject = "MyCloud";
        String content = "您正在注册 MyCloud, 验证码为：" + randomNumber + ", 5分钟内有效\n";
        tbValidcodeService.insertOrUpdate(email, String.valueOf(randomNumber));
        emailManager.sendEmail(subject, content, Collections.singleton(email));
        return ResponseEntity.success("验证码邮件已发送，请注意查收");
    }

    @PostMapping("/validcode/resetPassword/{email}")
    public ResponseEntity<String> sendResetPasswordCode(@PathVariable String email) {
        int randomNumber = new Random().nextInt(9999 - 1000 + 1) + 1000;
        String subject = "MyCloud";
        String content = "您正在进行 MyCloud 账号的密码重置, 验证码为：" + randomNumber + ", 5分钟内有效\n";
        tbValidcodeService.insertOrUpdate(email, String.valueOf(randomNumber));
        emailManager.sendEmail(subject, content, Collections.singleton(email));
        return ResponseEntity.success("验证码邮件已发送，请注意查收");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String validcode = data.get("validcode");
        String email = data.get("email");
        if (username == null || password == null || validcode == null || email == null) {
            throw new RuntimeException("注册信息不完整");
        }
        User user = userService.signup(username, password, email, validcode);
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
