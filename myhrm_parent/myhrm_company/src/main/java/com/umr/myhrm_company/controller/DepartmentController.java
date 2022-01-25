package com.umr.myhrm_company.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.umr.myhrm_common.controller.BaseController;
import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common_model.domain.company.Company;
import com.umr.myhrm_common_model.domain.company.Department;
import com.umr.myhrm_common_model.domain.company.response.DeptListResult;
import com.umr.myhrm_company.service.CompanyService;
import com.umr.myhrm_company.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门服务接口
 */
@CrossOrigin
@RestController
@RequestMapping("/company") // company/department
public class DepartmentController extends BaseController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    CompanyService companyService;

    /**
     * 查找对应部门
     * @param code 部门编码
     * @param companyId 企业id
     * @return
     */
    @PostMapping("/department/getByCode")
    public Department getByCode(@RequestParam("code") String code, @RequestParam("companyId") String companyId) {
        Department department = departmentService.getOne(new QueryWrapper<Department>()
                .eq("code", code)
                .eq("company_id", companyId));
        return department;
    }

    /**
     * 保存部门信息
     *
     * @param department
     * @return
     */
    @PostMapping("/department")
    public Result save(@RequestBody Department department) {
        //设置部门所属企业id
        department.setCompanyId(companyId);
        //保存部门信息
        boolean flag = departmentService.save(department);
        //返回页面结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查找当前登录用户companyId下的所有部门
     * @return
     */
    @GetMapping("/department")
    public Result list() throws NoResultException {
        //暂未解决问题：平台管理员登陆时，应返回公司结构列表显示在前端
        if (StringUtils.isEmpty(companyId))
            throw new NoResultException(ResultCode.ADMIN_DEPARTMENT);
        //查询用户所属企业
        Company company = companyService.getById(companyId);
        //查找该企业所有部门列表
        List<Department> departments = departmentService
                .list(new QueryWrapper<Department>()
                        .eq("company_id", companyId));
        //返回结果
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(new DeptListResult(company, departments));
        return result;
    }

    @GetMapping("/department/{id}")
    public Result getById(@PathVariable String id) {
        Department department = departmentService.getById(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(department);
        return result;
    }

    @PutMapping("/department/{id}")
    public Result update(@PathVariable String id, @RequestBody Department department) {
        department.setId(id);
        boolean flag = departmentService
                .update(department, new QueryWrapper<Department>()
                        .eq("id", id));
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping("/department/{id}")
    public Result delete(@PathVariable String id) {
        boolean flag = departmentService.removeById(id);
        return new Result(ResultCode.SUCCESS);
    }
}
