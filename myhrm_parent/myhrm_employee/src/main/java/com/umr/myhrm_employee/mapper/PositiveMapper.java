package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.EmployeePositive;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工转正申请持久层
 */
@Mapper
public interface PositiveMapper extends BaseMapper<EmployeePositive> {
}
