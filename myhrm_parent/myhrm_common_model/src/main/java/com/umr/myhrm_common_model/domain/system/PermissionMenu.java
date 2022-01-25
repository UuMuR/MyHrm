package com.umr.myhrm_common_model.domain.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 菜单权限资源实体类
 */
@TableName("pe_permission_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionMenu implements Serializable {
    private static final long serialVersionUID = -1002411490113957485L;

    /**
     * 主键
     */
    @TableId
    private String id;

    //展示图标
    private String menuIcon;

    //排序号
    private String menuOrder;
}