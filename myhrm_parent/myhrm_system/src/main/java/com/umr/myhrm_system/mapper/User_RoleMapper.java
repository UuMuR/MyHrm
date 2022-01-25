package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.User_Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色映射关系表持久层
 */
@Mapper
public interface User_RoleMapper extends BaseMapper<User_Role> {
}
