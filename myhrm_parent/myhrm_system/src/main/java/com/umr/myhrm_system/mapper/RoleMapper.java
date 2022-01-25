package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色表持久层
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
