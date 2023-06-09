package org.looko.mycloud.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.user.domain.User;
import org.looko.mycloud.user.mapper.UserMapper;
import org.looko.mycloud.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author aiden
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-27 15:32:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public List<User> listAll(Page<User> page) {
        return baseMapper.listAll(page);
    }

}




