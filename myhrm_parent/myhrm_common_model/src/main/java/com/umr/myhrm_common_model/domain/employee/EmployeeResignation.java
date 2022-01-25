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
 * 员工离职申请
 */
@TableName("em_resignation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResignation implements Serializable {
    private static final long serialVersionUID = 2890789302883962744L;
    /**
     * 员工Id
     */
    @TableId
    private String userId;
    /**
     * 离职时间
     */
    private String resignationTime;
    /**
     * 离职类型
     */
    private String typeOfTurnover;
    /**
     * 申请离职原因
     */
    private String reasonsForLeaving;
    /**
     * 补偿金
     */
    private String compensation;
    /**
     * 代通知金
     */
    private String notifications;
    /**
     * 社保减员月
     */
    private String socialSecurityReductionMonth;
    /**
     * 公积金减员月
     */
    private String providentFundReductionMonth;
    /**
     * 图片
     */
    private String picture;
    /**
     * 创建时间
     */
    private Date createTime;
}
