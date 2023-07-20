package org.looko.mycloud.user.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistrationFormVO(
        @NotBlank(message = "用户名不能为空") String username,
        @NotBlank(message = "密码不能为空") String password,
        @Pattern(regexp = "[0-9]{6}", message = "验证码格式错误") String validcode,
        @Email(message = "邮箱格式有误") String email
) {}
