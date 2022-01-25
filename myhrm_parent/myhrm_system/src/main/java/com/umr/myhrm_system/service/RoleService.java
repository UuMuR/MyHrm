package com.umr.myhrm_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.response.RoleResult;

import java.util.List;

public interface RoleService extends IService<Role> {
    Role getRole(String roleId);
    RoleResult getOne(String roleId);
    boolean savePermission(String roleId, List<String> permIds);
    IPage<Role> getPage(String companyId, int page, int size);
    boolean removeById(String id);
}
