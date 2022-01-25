package com.umr.myhrm_common_model.domain.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户与角色映射关系
 */
@TableName("pe_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User_Role {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;
}
