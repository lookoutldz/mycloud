package org.looko.mycloud.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.commonstarter.util.PatternUtils;
import org.looko.mycloud.user.constant.Messages;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.mapper.UserMapper;
import org.looko.mycloud.user.service.AuthorizationService;
import org.looko.mycloud.user.util.EmailUtils;
import org.looko.mycloud.user.util.ValidcodeUtils;
import org.looko.mycloud.user.vo.RegistrationFormVO;
import org.looko.mycloud.user.vo.ResetPasswordFormVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.looko.mycloud.user.constant.Parameters.dLockPrefix;
import static org.looko.mycloud.user.enumeration.BusinessTypeEnum.*;

@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserMapper userMapper;

    private final EmailUtils emailUtils;

    private final ValidcodeUtils validcodeUtils;

    private final RedissonClient redissonClient;

    public AuthorizationServiceImpl(UserMapper userMapper, EmailUtils emailUtils, ValidcodeUtils validcodeUtils, RedissonClient redissonClient) {
        this.userMapper = userMapper;
        this.emailUtils = emailUtils;
        this.validcodeUtils = validcodeUtils;
        this.redissonClient = redissonClient;
    }

    /**
     * login
     * 于 SecurityConfiguration 处设置此类 (? implements UserDetailsService) 作为登录校验处理类
     * 此方法将于登录时被调用
     * @param usernameOrEmail the username identifying the user whose data is required.
     * @return UserDetails
     * @throws UsernameNotFoundException 用户名不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if (usernameOrEmail == null) {
            throw new UsernameNotFoundException(Messages.wrongUsernameOrPassword);
        }
        User user;
        if (PatternUtils.emailPattern.matcher(usernameOrEmail).matches()) {
            user = userMapper.getByEmail(usernameOrEmail);
        } else {
            user = userMapper.getByUsername(usernameOrEmail);
        }
        if (user == null) {
            throw new UsernameNotFoundException(Messages.wrongUsernameOrPassword);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("admin")
                .build();
    }

    /**
     * 注册
     * @param form 注册表单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(RegistrationFormVO form) {

        if (userMapper.checkExistByUsername(form.username())) {
            throw new RuntimeException(Messages.usernameExist);
        }
        if (userMapper.checkExistByEmail(form.email())) {
            throw new RuntimeException(Messages.emailExist);
        }

        User user = new User();
        String storeKey = validcodeUtils.genStoreKey(VALIDCODE_REGISTER, form.email());
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                if (validcodeUtils.checkValidcodeFailed(VALIDCODE_REGISTER, form.email(), form.validcode())) {
                    throw new RuntimeException(Messages.wrongValidcode);
                }
                // 创建用户
                user.setUsername(form.username());
                user.setPassword(new BCryptPasswordEncoder().encode(form.password()));
                user.setEmail(form.email());
                userMapper.insert(user);
                // 创建成功后删除验证码
                validcodeUtils.deleteValidcode(VALIDCODE_REGISTER, form.email());
                // 所有操作顺利完成
                log.info("用户注册成功：id: {}, username: {}, email: {}", user.getId(), user.getUsername(), user.getEmail());
                return true;
            } catch (Exception e) {
                log.error("用户注册失败：id: {}, username: {}, email: {}", user.getId(), user.getUsername(), user.getEmail());
                throw e;
            } finally {
                rLock.unlock();
            }

        }
        // 没抢到锁，返回失败标记
        return false;
    }

    /**
     * 重置密码
     * @param form 重置密码表单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(ResetPasswordFormVO form) {
        String storeKey = validcodeUtils.genStoreKey(VALIDCODE_RESET_PASSWORD, form.email());
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                if (validcodeUtils.checkValidcodeFailed(VALIDCODE_RESET_PASSWORD, form.email(), form.validcode())) {
                    throw new RuntimeException(Messages.wrongValidcode);
                }
                // 重置密码
                userMapper.resetPassword(form.email(), new BCryptPasswordEncoder().encode(form.password()));
                // 重置成功后删除验证码
                validcodeUtils.deleteValidcode(VALIDCODE_RESET_PASSWORD, form.email());
                // 所有操作顺利完成
                log.info("重置密码成功：email: {}", form.email());
                return true;
            } catch (Exception e) {
                log.error("重置密码失败：email: {}", form.email());
                throw e;
            } finally {
                rLock.unlock();
            }
        }
        // 没抢到锁，返回失败标记
        return false;
    }

    /**
     * 验证码 - 注册
     * @param email 收件邮箱
     */
    @Override
    public Boolean sendRegisterValidcode(String email) {
        return commonSendValidcode(VALIDCODE_REGISTER, email);
    }

    /**
     * 验证码 - 登录
     * @param usernameOrEmail 用户名或邮箱
     */
    @Override
    public Boolean sendLoginValidcode(String usernameOrEmail) {
        // 确定邮箱地址
        String email;
        if (PatternUtils.emailPattern.matcher(usernameOrEmail).matches()) {
            email = usernameOrEmail;
        } else {
            User user = userMapper.getByUsername(usernameOrEmail);
            if (user == null) {
                throw new RuntimeException(Messages.userNotFound);
            }
            email = user.getEmail();
        }

        return commonSendValidcode(VALIDCODE_LOGIN, email);
    }

    /**
     * 验证码 - 重置密码
     * @param email 收件邮箱
     */
    @Override
    public Boolean sendResetPasswordValidcode(String email) {
        // 校验该邮箱的用户是否存在
        if (userMapper.getByEmail(email) == null) {
            throw new RuntimeException(Messages.userNotFound);
        }

        return commonSendValidcode(VALIDCODE_RESET_PASSWORD, email);
    }

    private boolean commonSendValidcode(BusinessTypeEnum businessTypeEnum, String email) {
        String storeKey = validcodeUtils.genStoreKey(businessTypeEnum, email);
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                String validcode = validcodeUtils.genValidcode();
                // 验证码存到 redis 待后续逻辑取用
                validcodeUtils.saveValidcode(businessTypeEnum, email, validcode);
                // 发送一个邮件任务到消息队列
                emailUtils.publishValidcodeEmail(businessTypeEnum, email, validcode);
                // 所有操作顺利完成
                return true;
            } catch (Exception e) {
                log.error("邮件发送异常", e);
                throw e;
            } finally {
                rLock.unlock();
            }
        }
        // 没抢到锁，返回失败标记
        return false;
    }
}

