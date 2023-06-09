package org.looko.mycloud.user.service;

import org.looko.mycloud.user.vo.RegistrationFormVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizationService extends UserDetailsService {

    /**
     * 发送注册验证码
     * @param email 收件邮箱
     */
    Boolean sendRegisterValidcode(String email);

    /**
     * 注册
     * @param form 注册表单
     */
    Boolean register(RegistrationFormVO form);

    /**
     * 发送登录验证码
     * @param usernameOrEmail 用户名或邮箱
     */
    Boolean sendLoginValidcode(String usernameOrEmail);

    /**
     * 发送重置密码验证码
     * @param email 收件邮箱
     */
    Boolean sendResetPasswordValidcode(String email);

    /**
     * 重置密码
     * @param form 注册表单(仅含重置密码所需的必要信息)
     */
    Boolean resetPassword(RegistrationFormVO form);
}
