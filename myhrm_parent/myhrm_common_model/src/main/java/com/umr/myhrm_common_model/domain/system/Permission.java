package com.umr.myhrm_common_model.domain.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 权限实体类
 * 与api，menu，point共享主键
 */
@TableName("pe_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements Serializable {
    private static final long serialVersionUID = -4990810027542971546L;
    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限类型 1为菜单 2为功能 3为API
     */
    private Integer type;

    /**
     * 权限编码
     * 根据此条目判断是否享有权限
     */
    private String code;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 父类id
     * 权限包含上下级关系
     */
    private String pid;

    /**
     * 可见性 -- 企业是否可见
     */
    private Integer enVisible;

    public Permission(String name, Integer type, String code, String description) {
        this.name = name;
        this.type = type;
        this.code = code;
        this.description = description;
    }


}