package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.PermissionPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 按钮权限持久层
 */
@Mapper
public interface PermissionPointMapper extends BaseMapper<PermissionPoint> {
}
