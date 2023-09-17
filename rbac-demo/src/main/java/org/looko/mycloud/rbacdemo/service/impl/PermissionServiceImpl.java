package org.looko.mycloud.rbacdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.rbacdemo.domain.Permission;
import org.looko.mycloud.rbacdemo.service.PermissionService;
import org.looko.mycloud.rbacdemo.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【permission】的数据库操作Service实现
* @createDate 2023-08-25 13:19:51
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




