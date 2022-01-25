package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.EmployeeResignation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工离职申请持久层
 */
@Mapper
public interface ResignationMapper extends BaseMapper<EmployeeResignation> {
}
