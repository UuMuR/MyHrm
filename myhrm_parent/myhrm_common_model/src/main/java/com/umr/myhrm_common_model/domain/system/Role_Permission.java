package com.umr.myhrm_common_model.domain.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色与权限映射关系
 */
@TableName("pe_role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role_Permission {
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;
}
