package com.umr.myhrm_company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.umr.myhrm_common_model.domain.company.Company;
import com.umr.myhrm_company.mapper.CompanyMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class MyhrmCompanyApplicationTests {

    @Autowired
    CompanyMapper companyMapper;

    @Test
    void testMapperInsert() {
        Company company = new Company();
        company.setId("1");
        company.setName("aTest");
        company.setManagerId("1");
        company.setCompanyArea("1");
        company.setBalance(1.00);
        company.setVersion("1");
        company.setCompanyPhone("123");
        company.setCompanySize("123");
        company.setCompanyAddress("123");
        company.setAuditState("123");
        company.setBusinessLicenseId("123");
        company.setCreateTime(new Date());
        company.setExpirationDate(new Date());
        company.setRemarks("123456");
        company.setLegalRepresentative("123456");
        company.setMailbox("123143253");
        company.setRenewalDate(new Date());
        company.setIndustry("5543");
        company.setState(1);
        companyMapper.insert(company);
    }

    @Test
    void testMapper() {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", "1");
        Company company = companyMapper.selectOne(queryWrapper);
        System.out.println(company);
        company.setName("test");
        companyMapper.delete(queryWrapper);
    }

}
