package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common_model.domain.employee.EmployeePositive;
import com.umr.myhrm_employee.mapper.PositiveMapper;
import com.umr.myhrm_employee.service.PositiveService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 员工转正申请业务逻辑
 */
@Service
public class PositiveServiceImpl extends ServiceImpl<PositiveMapper, EmployeePositive> implements PositiveService {

    /**
     * 保存申请
     * @param positive
     * @return
     */
    @Override
    public boolean save(EmployeePositive positive) {
        positive.setCreateTime(new Date());
        positive.setEstatus(1); //未执行
        return super.save(positive);
    }
}
