package com.umr.myhrm_employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.employee.UserCompanyJobs;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业岗位信息持久层
 */
@Mapper
public interface UserCompanyJobsMapper extends BaseMapper<UserCompanyJobs> {
}
