package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.PermissionMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限持久层
 */
@Mapper
public interface PermissionMenuMapper extends BaseMapper<PermissionMenu> {
}
