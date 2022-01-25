package com.umr.myhrm_feign.client;

import com.umr.myhrm_common_model.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 部门服务feign接口
 */
@FeignClient("myhrm-company")
public interface DepartmentClient {

    @PostMapping("/company/department/getByCode")
    Department getByCode(@RequestParam("code") String code, @RequestParam("companyId") String companyId);

}
