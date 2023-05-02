package org.looko.mycloud.user.service;

import org.looko.mycloud.user.domain.TbValidcode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author aiden
* @description 针对表【tb_validcode】的数据库操作Service
* @createDate 2023-05-01 14:12:21
*/
public interface TbValidcodeService extends IService<TbValidcode> {

    void insertOrUpdate(String email, String validcode);

    Boolean checkValidcode(String email, String validcode);
}
