package com.umr.myhrm_company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.company.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门信息持久层
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}
