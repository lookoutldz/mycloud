package org.looko.mycloud.user.service;

import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.vo.RegistrationFormVO;
import org.looko.mycloud.user.vo.ResetPasswordFormVO;

public interface AuthorizationService {


    User getByUsernameOrEmail(String usernameOrEmail);

    /**
     * 注册
     * @param form 注册表单
     */
    Boolean register(RegistrationFormVO form);

    /**
     * 重置密码
     * @param form 重置密码表单
     */
    Boolean resetPassword(ResetPasswordFormVO form);

    /**
     * 发送注册验证码
     * @param email 收件邮箱
     */
    Boolean sendRegisterValidcode(String email);

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

}
