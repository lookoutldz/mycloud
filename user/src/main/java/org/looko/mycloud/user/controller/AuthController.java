package org.looko.mycloud.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.entity.ResponseEntity;
import org.looko.mycloud.user.service.AuthorizationService;
import org.looko.mycloud.user.constant.Messages;
import org.looko.mycloud.user.vo.RegistrationFormVO;
import org.looko.mycloud.user.vo.ResetPasswordFormVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@Validated
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
    public ResponseEntity<String> sendRegisterValidcode(@PathVariable @Email String email) {
        if (authorizationService.sendRegisterValidcode(email)) {
            return ResponseEntity.success(Messages.validcodeHasBeenSent);
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 验证码接口 - 重置密码验证码
     * @param email 接收验证码的邮箱
     */
    @PostMapping("/validcode/resetPassword/{email}")
    public ResponseEntity<String> sendResetPasswordValidcode(@PathVariable @Email String email) {
        if (authorizationService.sendResetPasswordValidcode(email)) {
            return ResponseEntity.success(Messages.validcodeHasBeenSent);
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 注册
     * @param form 注册表单
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationFormVO form) {
        if (authorizationService.register(form)) {
            return ResponseEntity.success(Messages.registerSuccess);
        } else {
            return ResponseEntity.failure();
        }
    }

    /**
     * 重置密码
     * @param form 注册表单
     */
    @PostMapping("/resetPassword/")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordFormVO form) {
        if (authorizationService.resetPassword(form)) {
            return ResponseEntity.success(Messages.resetPasswordSuccess);
        } else {
            return ResponseEntity.failure();
        }
    }
}
