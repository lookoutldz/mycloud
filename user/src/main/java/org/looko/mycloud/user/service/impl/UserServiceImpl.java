package org.looko.mycloud.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.mapper.TbValidcodeMapper;
import org.looko.mycloud.user.service.UserService;
import org.looko.mycloud.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author aiden
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-27 15:32:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private TbValidcodeMapper tbValidcodeMapper;

    @Override
    public List<User> listAll(Page<User> page) {
        return baseMapper.listAll(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User signup(String username, String password, String email, String validcode) {
        if (baseMapper.checkExistByUsername(username)) {
            throw new RuntimeException("该用户名已存在");
        }
        if (baseMapper.checkExistByEmail(email)) {
            throw new RuntimeException("该邮箱用户已注册");
        }
        if (!tbValidcodeMapper.checkValidcode(email, validcode)) {
            throw new RuntimeException("验证码错误");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);
        baseMapper.insert(user);
        tbValidcodeMapper.expireValidcode(email, validcode);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetPassword(String email, String password, String validcode) {
        if (!tbValidcodeMapper.checkValidcode(email, validcode)) {
            throw new RuntimeException("验证码错误");
        }
        baseMapper.resetPassword(email, new BCryptPasswordEncoder().encode(password));
        tbValidcodeMapper.expireValidcode(email, validcode);
    }
}




