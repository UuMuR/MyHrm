package com.umr.myhrm_company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.company.Company;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公司信息持久层
 */
@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
}
