package org.looko.mycloud.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.looko.mycloud.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author aiden
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-27 15:33:00
*/
public interface UserService extends IService<User> {

    List<User> listAll(Page<User> page);
}
