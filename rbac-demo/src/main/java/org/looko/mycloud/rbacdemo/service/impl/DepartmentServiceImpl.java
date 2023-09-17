package org.looko.mycloud.rbacdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.looko.mycloud.rbacdemo.domain.Department;
import org.looko.mycloud.rbacdemo.service.DepartmentService;
import org.looko.mycloud.rbacdemo.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

/**
* @author aiden
* @description 针对表【department】的数据库操作Service实现
* @createDate 2023-08-25 13:19:51
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService{

}




