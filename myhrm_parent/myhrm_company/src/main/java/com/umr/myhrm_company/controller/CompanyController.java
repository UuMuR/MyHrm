package com.umr.myhrm_company.controller;

import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common_model.domain.company.Company;
import com.umr.myhrm_company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 企业服务接口
 */
@CrossOrigin // 解决跨域问题
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    /**
     * 保存企业信息
     * @param company 企业信息
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Company company) {
        boolean flag = companyService.save(company);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 更新企业信息
     * @param id 企业id
     * @param company 更新信息
     * @return
     */
    @PutMapping("{id}")
    public Result update(@PathVariable String id, @RequestBody Company company) {
        company.setId(id);
        boolean flag = companyService.updateById(company);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 根据id删除企业信息
     * @param id 企业id
     * @return
     */
    @DeleteMapping("{id}")
    public Result delete(@PathVariable String id) {
        boolean flag = companyService.removeById(id);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 根据id查询
     * @param id 企业id
     * @return
     */
    @GetMapping("{id}")
    public Result getById(@PathVariable String id) throws NoResultException {
        Company company = companyService.getById(id);
        if (company == null) {
            throw new NoResultException(ResultCode.NONE_RESULT);
        }
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }

    /**
     * 查询全部企业信息
     * @return
     */
    @GetMapping
    public Result getList() {
        List<Company> companies = companyService.list();
        if (companies.size() != 0) {
            Result result = new Result(ResultCode.SUCCESS);
            result.setData(companies);
            return result;
        }
        return new Result(ResultCode.FAIL);
    }

}
