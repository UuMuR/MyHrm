package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common_model.domain.employee.EmployeeResignation;
import com.umr.myhrm_common_model.domain.employee.UserCompanyPerson;
import com.umr.myhrm_common_model.domain.employee.response.EmployeeReportResult;
import com.umr.myhrm_employee.mapper.ResignationMapper;
import com.umr.myhrm_employee.mapper.UserCompanyPersonMapper;
import com.umr.myhrm_employee.service.UserCompanyPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 员工企业业务逻辑层
 */
@Service
public class UserCompanyPersonServiceImpl extends ServiceImpl<UserCompanyPersonMapper, UserCompanyPerson> implements UserCompanyPersonService {

    @Autowired
    UserCompanyPersonMapper userCompanyPersonMapper;
    @Autowired
    ResignationMapper resignationMapper;

    /**
     * 查找入职时间为 month 的用户信息
     * @param month “yyyy-mm”
     * @return
     */
    @Override
    public List<EmployeeReportResult> list(String month, String companyId) throws InvocationTargetException, IllegalAccessException {
        Set<String> ids = new HashSet<>();
        //添加入职人员信息
        List<UserCompanyPerson> userList = userCompanyPersonMapper
                .selectList(new QueryWrapper<UserCompanyPerson>()
                .like("time_of_entry", month).eq("company_id", companyId));
        List<EmployeeReportResult> reportList = new ArrayList<>();
        for (UserCompanyPerson user : userList) {
            EmployeeResignation resignation = resignationMapper.selectById(user.getUserId());
            EmployeeReportResult report = new EmployeeReportResult(user, resignation);
            ids.add(user.getUserId());
            reportList.add(report);
        }
        //添加离职人员信息
        List<EmployeeResignation> resignations = resignationMapper
                .selectList(new QueryWrapper<EmployeeResignation>()
                .like("resignation_time", month));
        for (EmployeeResignation resignation : resignations) {
            if (ids.contains(resignation.getUserId())) continue; //已经查询过，跳过
            UserCompanyPerson user = userCompanyPersonMapper.selectById(resignation.getUserId());
            EmployeeReportResult report = new EmployeeReportResult(user, resignation);
            reportList.add(report);
        }
        return reportList;
    }
}
