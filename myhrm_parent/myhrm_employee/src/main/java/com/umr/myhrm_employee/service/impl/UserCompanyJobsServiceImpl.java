package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common_model.domain.employee.UserCompanyJobs;
import com.umr.myhrm_employee.mapper.UserCompanyJobsMapper;
import com.umr.myhrm_employee.service.UserCompanyJobsService;
import org.springframework.stereotype.Service;

/**
 * 岗位信息业务逻辑
 */
@Service
public class UserCompanyJobsServiceImpl extends ServiceImpl<UserCompanyJobsMapper, UserCompanyJobs> implements UserCompanyJobsService {
}
