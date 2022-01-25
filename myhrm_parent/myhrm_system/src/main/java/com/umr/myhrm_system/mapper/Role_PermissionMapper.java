package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.Role_Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与权限映射表持久层
 */
@Mapper
public interface Role_PermissionMapper extends BaseMapper<Role_Permission> {
}
