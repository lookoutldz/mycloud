package org.looko.mycloud.user.mapper;

import org.looko.mycloud.user.domain.TbValidcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author aiden
* @description 针对表【tb_validcode】的数据库操作Mapper
* @createDate 2023-05-01 14:12:21
* @Entity org.looko.mycloud.user.domain.TbValidcode
*/
@Repository
public interface TbValidcodeMapper extends BaseMapper<TbValidcode> {

    void insertOrUpdate(String email, String validcode);

    Boolean checkValidcode(String email, String validcode);

    void expireValidcode(String email, String validcode);
}




