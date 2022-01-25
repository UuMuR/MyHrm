package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.EmployeeArchive;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工档案信息持久层
 */
@Mapper
public interface ArchiveMapper extends BaseMapper<EmployeeArchive> {
}
