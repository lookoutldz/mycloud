package org.looko.mycloud.rbacdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.rbacdemo.domain.Contract;
import org.looko.mycloud.rbacdemo.service.ContractService;
import org.looko.mycloud.rbacdemo.mapper.ContractMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【contract】的数据库操作Service实现
* @createDate 2023-08-25 13:19:51
*/
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract>
    implements ContractService{

}




