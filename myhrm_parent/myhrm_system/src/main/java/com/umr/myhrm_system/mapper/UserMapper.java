package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
