package com.umr.myhrm_company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common_model.domain.company.Company;
import com.umr.myhrm_company.mapper.CompanyMapper;
import com.umr.myhrm_company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 企业服务实现类
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    /**
     * id生成器
     */
    @Autowired
    IdWorker idWorker;

    /**
     * 重写保存企业方法 -- 为企业生成自定的id（基于雪花算法）
     * @param company 企业实体
     * @return 返回是否添加成功
     */
    @Override
    public boolean save(Company company) {
        company.setId(String.valueOf(idWorker.nextId())); //id
        company.setState(1); //已激活
        company.setAuditState("0"); //未审核

        company.setCreateTime(new Date());
        company.setExpirationDate(new Date());
        company.setRenewalDate(new Date());

        return super.save(company);
    }
}
