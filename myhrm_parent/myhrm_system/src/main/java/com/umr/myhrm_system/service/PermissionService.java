package com.umr.myhrm_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.umr.myhrm_common_model.domain.system.Permission;
import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {

    boolean save(Map<String, Object> map);
    boolean update(Map<String, Object> map, String id);
    List<Permission> getList(Map<String, Object> map);
    boolean deleteById(String id);
}
