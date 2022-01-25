package com.umr.myhrm_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.umr.myhrm_common_model.domain.system.PermissionApi;
import org.apache.ibatis.annotations.Mapper;

/**
 * Api类权限持久层
 */
@Mapper
public interface PermissionApiMapper extends BaseMapper<PermissionApi> {
}
