package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.EmployeeTransferPosition;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调岗申请持久层
 */
@Mapper
public interface TransferPositionMapper extends BaseMapper<EmployeeTransferPosition> {
}
