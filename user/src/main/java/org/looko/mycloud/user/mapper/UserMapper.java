package org.looko.mycloud.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.looko.mycloud.user.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author aiden
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-04-27 15:33:00
* @Entity org.looko.mycloud.user.domain.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> listAll(Page<User> page);

    User getByUsername(String username);

    User getByEmail(String email);

    Boolean checkExistByEmail(String email);

    Boolean checkExistByUsername(String username);

    void resetPassword(String email, String password);
}




