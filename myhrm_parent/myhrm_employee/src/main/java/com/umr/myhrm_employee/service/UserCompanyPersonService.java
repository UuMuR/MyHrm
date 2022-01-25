package com.umr.myhrm_employee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.umr.myhrm_common_model.domain.employee.UserCompanyPerson;
import com.umr.myhrm_common_model.domain.employee.response.EmployeeReportResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserCompanyPersonService extends IService<UserCompanyPerson> {

    List<EmployeeReportResult> list(String month, String companyId) throws InvocationTargetException, IllegalAccessException;
}
