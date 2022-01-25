package com.umr.myhrm_common_model.domain.company.response;

import com.umr.myhrm_common_model.domain.company.Company;
import com.umr.myhrm_common_model.domain.company.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 部门列表返回对象
 * companyId -- 部门所属企业id
 * companyName -- 企业名称
 * companyManager -- 企业管理员
 * departments -- 企业列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManager;
    private List<Department> depts;

    /**
     * 带企业信息构造方法
     * @param company 所属企业信息
     * @param departments 部门列表
     */
    public DeptListResult(Company company, List<Department> departments) {
        this.depts = departments;
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManager = company.getManagerId();
    }
}
