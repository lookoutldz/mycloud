package org.looko.mycloud.user.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordFormVO(
        @NotBlank String password,
        @Pattern(regexp = "[0-9]{6}", message = "验证码格式错误") String validcode,
        @Email String email
) {
}
