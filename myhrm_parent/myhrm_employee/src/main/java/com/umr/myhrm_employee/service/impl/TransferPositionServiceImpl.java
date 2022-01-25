package com.umr.myhrm_employee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common_model.domain.employee.EmployeeTransferPosition;
import com.umr.myhrm_employee.mapper.TransferPositionMapper;
import com.umr.myhrm_employee.service.TransferPositionService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 调岗申请业务逻辑层
 */
@Service
public class TransferPositionServiceImpl extends ServiceImpl<TransferPositionMapper, EmployeeTransferPosition> implements TransferPositionService {

    /**
     * 保存调岗申请
     * @param transfer
     * @return
     */
    @Override
    public boolean save(EmployeeTransferPosition transfer) {
        transfer.setCreateTime(new Date());
        transfer.setEstatus(1); //未执行
        return super.save(transfer);
    }
}
