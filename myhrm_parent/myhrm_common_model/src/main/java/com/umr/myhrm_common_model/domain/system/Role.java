package com.umr.myhrm_common_model.domain.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@TableName("pe_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
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
     * 拥有该角色的用户id
     */
    @JsonIgnore
    @TableField(exist = false)
//    @ManyToMany(mappedBy="roles")
    private Set<User> users;//角色与用户   多对多

    /**
     * 角色绑定的权限
     */
    @JsonIgnore
    @TableField(exist = false)
//    @ManyToMany
//    @JoinTable(name="pe_role_permission",
//            joinColumns={@JoinColumn(name="role_id",referencedColumnName="id")},
//            inverseJoinColumns={@JoinColumn(name="permission_id",referencedColumnName="id")})
    private Set<Permission> permissions;
}