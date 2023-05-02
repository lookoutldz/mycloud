package org.looko.mycloud.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.user.domain.TbValidcode;
import org.looko.mycloud.user.service.TbValidcodeService;
import org.looko.mycloud.user.mapper.TbValidcodeMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【tb_validcode】的数据库操作Service实现
* @createDate 2023-05-01 14:12:21
*/
@Service
public class TbValidcodeServiceImpl extends ServiceImpl<TbValidcodeMapper, TbValidcode>
    implements TbValidcodeService{

    @Override
    public void insertOrUpdate(String email, String validcode) {
        baseMapper.insertOrUpdate(email, validcode);
    }

    @Override
    public Boolean checkValidcode(String email, String validcode) {
        return baseMapper.checkValidcode(email, validcode);
    }
}




