package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表持久层
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
