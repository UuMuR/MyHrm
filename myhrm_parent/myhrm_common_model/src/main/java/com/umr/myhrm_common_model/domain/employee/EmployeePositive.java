package com.umr.myhrm_common_model.domain.employee;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 员工转正申请
 */
@TableName("em_positive")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePositive implements Serializable {
    private static final long serialVersionUID = 2391824518947910773L;
    /**
     * 员工ID
     */
    @TableId
    private String userId;
    /**
     * 转正日期
     */
    private Date dateOfCorrection;
    /**
     * 转正评价
     */
    private String correctionEvaluation;
    /**
     * 附件
     */
    private String enclosure;
    /**
     * 单据状态 1是未执行，2是已执行
     */
    private Integer estatus;
    /**
     * 创建时间
     */
    private Date createTime;
}
