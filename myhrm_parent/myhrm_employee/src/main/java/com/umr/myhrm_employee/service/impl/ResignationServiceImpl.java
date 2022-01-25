package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common_model.domain.employee.EmployeeResignation;
import com.umr.myhrm_employee.mapper.ResignationMapper;
import com.umr.myhrm_employee.service.ResignationService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 员工离职申请业务逻辑
 */
@Service
public class ResignationServiceImpl extends ServiceImpl<ResignationMapper, EmployeeResignation> implements ResignationService {

    /**
     * 保存离职申请
     * @param resignation
     * @return
     */
    @Override
    public boolean save(EmployeeResignation resignation) {
        resignation.setCreateTime(new Date());
        return super.save(resignation);
    }
}
