package org.looko.mycloud.user.service.impl;

import org.looko.mycloud.commonstarter.util.PatternUtils;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.mapper.UserMapper;
import org.looko.mycloud.user.service.AuthorizationService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserMapper userMapper;

    public AuthorizationServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 于 SecurityConfiguration 处设置此类 (? implements UserDetailsService) 作为登录校验处理类
     * 此方法将于登录时被调用
     * @param username the username identifying the user whose data is required.
     * @return UserDetails
     * @throws UsernameNotFoundException 用户名不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        User user;
        if (PatternUtils.emailPattern.matcher(username).matches()) {
            user = userMapper.getByEmail(username);
        } else {
            user = userMapper.getByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("admin")
                .build();
    }
}
