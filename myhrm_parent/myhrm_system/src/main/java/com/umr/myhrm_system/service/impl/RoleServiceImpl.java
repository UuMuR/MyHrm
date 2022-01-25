package com.umr.myhrm_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.Role_Permission;
import com.umr.myhrm_common_model.domain.system.User_Role;
import com.umr.myhrm_common_model.domain.system.response.RoleResult;
import com.umr.myhrm_system.mapper.PermissionMapper;
import com.umr.myhrm_system.mapper.RoleMapper;
import com.umr.myhrm_system.mapper.Role_PermissionMapper;
import com.umr.myhrm_system.mapper.User_RoleMapper;
import com.umr.myhrm_system.service.RoleService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色表业务逻辑
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    Role_PermissionMapper role_permissionMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    User_RoleMapper user_roleMapper;

    /**
     * 获取角色与其所有具体权限
     * @param id
     * @return
     */
    @Override
    public Role getRole(String id) {
        Role role = roleMapper.selectById(id);
        List<Role_Permission> role_permissions = role_permissionMapper
                .selectList(new QueryWrapper<Role_Permission>()
                        .eq("role_id", id));
        Set<Permission> perms = new HashSet<>();
        for (Role_Permission role_permission : role_permissions) {
            Permission permission = permissionMapper
                    .selectById(role_permission.getPermissionId());
            perms.add(permission);
        }
        role.setPermissions(perms);
        return role;
    }

    /**
     * 查询角色与其权限
     */
    @Override
    public RoleResult getOne(String roleId) {
        Role role = roleMapper.selectById(roleId);
        List<Role_Permission> role_permissions = role_permissionMapper
                .selectList(new QueryWrapper<Role_Permission>()
                        .eq("role_id", roleId));
        List<String> permIds = new ArrayList<>();
        for (Role_Permission role_permission : role_permissions) {
            permIds.add(role_permission.getPermissionId());
        }
        return new RoleResult(role, permIds);
    }

    /**
     * 绑定(更新)角色与权限
     * @param roleId 角色id
     * @param permIds 角色所有权限id
     * @return
     */
    @Override
    public boolean savePermission(String roleId, List<String> permIds) {
        //绑定前先删除角色当前所有权限
        role_permissionMapper.delete(new QueryWrapper<Role_Permission>()
                .eq("role_id", roleId));
        //绑定新权限
        for (String permId : permIds) {
            role_permissionMapper.insert(new Role_Permission(roleId, permId));
        }
        return true;
    }

    /**
     * 保存角色信息
     * @param role
     * @return
     */
    @Override
    public boolean save(Role role) {
        role.setId(String.valueOf(idWorker.nextId()));
        return super.save(role);
    }

    /**
     * 分页查询
     * @param companyId 企业id
     * @param
     * @param
     * @return
     */
    @Override
    public IPage<Role> getPage(String companyId, int page, int size) {
        IPage<Role> queryPage = new Page<>(page, size);
        IPage<Role> roleIPage = roleMapper.
                selectPage(queryPage, new QueryWrapper<Role>().
                        eq("company_id", companyId));
        return roleIPage;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(String id) {
        //删除角色所有权限映射
        role_permissionMapper.delete(new QueryWrapper<Role_Permission>().
                eq("role_id", id));
        //删除角色所有用户映射
        user_roleMapper.delete(new QueryWrapper<User_Role>().
                eq("role_id", id));
        //删除角色信息
        return roleMapper.deleteById(id) == 1;
    }
}
