package org.looko.mycloud.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.user.service.AuthorizationService;
import org.looko.mycloud.user.vo.RegistrationFormVO;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthorizationService authorizationService;
    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * 验证码接口 - 注册验证码
     * @param email 接收验证码的邮箱
     */
    @PostMapping("/validcode/register/{email}")
    public ResponseEntity<String> sendRegisterValidcode(@PathVariable String email) {
        // TODO 校验前端数据
        if (authorizationService.sendRegisterValidcode(email)) {
            return ResponseEntity.success("验证码邮件已发送，请注意查收");
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 验证码接口 - 重置密码验证码
     * @param email 接收验证码的邮箱
     */
    @PostMapping("/validcode/resetPassword/{email}")
    public ResponseEntity<String> sendResetPasswordValidcode(@PathVariable String email) {
        // TODO 校验前端数据
        if (authorizationService.sendResetPasswordValidcode(email)) {
            return ResponseEntity.success("验证码邮件已发送，请注意查收");
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 注册
     * @param form 注册表单
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationFormVO form) {
        // TODO 校验前端数据
        if (form.username() == null || form.password() == null || form.validcode() == null || form.email() == null) {
            throw new RuntimeException("注册信息不完整");
        }
        if (authorizationService.register(form)) {
            return ResponseEntity.success("注册成功");
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 重置密码
     * @param form 注册表单
     */
    @PostMapping("/resetPassword/")
    public ResponseEntity<String> resetPassword(@RequestBody RegistrationFormVO form) {
        // TODO 校验前端数据
        if (form.password() == null || form.validcode() == null || form.email() == null) {
            throw new RuntimeException("邮箱，密码或验证码为空");
        }
        if (authorizationService.resetPassword(form)) {
            return ResponseEntity.success("密码重置成功");
        } else {
            return ResponseEntity.failure();
        }
    }
}
