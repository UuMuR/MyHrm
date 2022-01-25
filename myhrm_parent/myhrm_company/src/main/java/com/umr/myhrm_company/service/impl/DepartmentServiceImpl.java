package com.umr.myhrm_company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common_model.domain.company.Department;
import com.umr.myhrm_company.mapper.DepartmentMapper;
import com.umr.myhrm_company.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 部门业务逻辑层
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    /**
     * id生成器
     */
    @Autowired
    IdWorker idWorker;

    /**
     * 重写保存部门方法 -- 为部门生成自定的id（雪花算法）
     * @param department 部门实体
     * @return 返回是否添加成功
     */
    @Override
    public boolean save(Department department) {

        department.setCreateTime(new Date());

        department.setId(String.valueOf(idWorker.nextId())); //位部门信息生成id
        return super.save(department);
    }

}
