package org.looko.mycloud.user.mapper;

import org.apache.ibatis.annotations.Param;
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

    void insertOrUpdate(@Param("email") String email,@Param("validcode") String validcode);

    Boolean checkValidcode(@Param("email") String email,@Param("validcode") String validcode);

    void expireValidcode(@Param("email") String email,@Param("validcode") String validcode);
}




