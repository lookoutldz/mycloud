package org.looko.mycloud.rbacdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.rbacdemo.domain.User;
import org.looko.mycloud.rbacdemo.service.UserService;
import org.looko.mycloud.rbacdemo.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-08-25 13:19:51
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




