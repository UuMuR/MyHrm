package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.UserCompanyPerson;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工企业详细信息持久层
 */
@Mapper
public interface UserCompanyPersonMapper extends BaseMapper<UserCompanyPerson> {
}
