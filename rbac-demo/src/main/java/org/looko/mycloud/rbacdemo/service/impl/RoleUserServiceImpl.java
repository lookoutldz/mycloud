package org.looko.mycloud.rbacdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.rbacdemo.domain.RoleUser;
import org.looko.mycloud.rbacdemo.service.RoleUserService;
import org.looko.mycloud.rbacdemo.mapper.RoleUserMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【role_user】的数据库操作Service实现
* @createDate 2023-08-25 15:40:17
*/
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser>
    implements RoleUserService{

}




