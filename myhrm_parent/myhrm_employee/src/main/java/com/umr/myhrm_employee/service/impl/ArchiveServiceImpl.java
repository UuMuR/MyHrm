package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common_model.domain.employee.EmployeeArchive;
import com.umr.myhrm_employee.mapper.ArchiveMapper;
import com.umr.myhrm_employee.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * 员工档案业务逻辑层
 */
@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, EmployeeArchive> implements ArchiveService {

    @Autowired
    IdWorker idWorker;

    /**
     * 为新增用户实体添加默认id
     * @param archive
     * @return
     */
    @Override
    public boolean save(EmployeeArchive archive) {
        archive.setCreateTime(new Date());
        archive.setId(String.valueOf(idWorker.nextId()));
        return super.save(archive);
    }
}
