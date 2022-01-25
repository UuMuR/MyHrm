package com.umr.myhrm_common_model.domain.system.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 角色查询返回对象 -- 包含角色的权限集合
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResult implements Serializable {
    private static final long serialVersionUID = 594829320797158219L;
    @TableId
    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 说明
     */
    private String description;
    /**
     * 企业id
     */
    private String companyId;

    /**
     * 角色所有权限
     */
    private List<String> permIds;

    public RoleResult(Role role, List<String> permIds) {
        this.permIds = permIds;
        BeanUtils.copyProperties(role, this);
    }

}