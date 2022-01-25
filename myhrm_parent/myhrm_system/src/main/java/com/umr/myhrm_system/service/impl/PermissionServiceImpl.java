package com.umr.myhrm_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common.utils.PermissionConstants;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_common_model.domain.system.PermissionApi;
import com.umr.myhrm_common_model.domain.system.PermissionMenu;
import com.umr.myhrm_common_model.domain.system.PermissionPoint;
import com.umr.myhrm_system.mapper.PermissionApiMapper;
import com.umr.myhrm_system.mapper.PermissionMapper;
import com.umr.myhrm_system.mapper.PermissionMenuMapper;
import com.umr.myhrm_system.mapper.PermissionPointMapper;
import com.umr.myhrm_system.service.PermissionService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 权限表业务逻辑层
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    IdWorker idWorker;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    PermissionApiMapper apiMapper;
    @Autowired
    PermissionMenuMapper menuMapper;
    @Autowired
    PermissionPointMapper pointMapper;
    /**
     * 添加权限
     * @param map 根据map中的type字段区分menu，api，point
     *            新建permission条目，并装入具体权限资源类
     *            权限与资源享有共同主键
     * @return
     */
    @Override
    public boolean save(Map<String, Object> map) {
        //返回结果
        int i = 0, j = 0;
        try {
            //共有主键
            String id = String.valueOf(idWorker.nextId());
            Permission perm = new Permission();
            //创建权限
            BeanUtils.populate(perm, map);
            perm.setId(id);
            i = permissionMapper.insert(perm);
            //资源类型
            int type = perm.getType();
            switch (type) {
                case PermissionConstants.PERMISSION_MENU:
                    //type == 1，菜单资源
                    PermissionMenu menu = new PermissionMenu();
                    BeanUtils.populate(menu, map);
                    menu.setId(id);
                    j = menuMapper.insert(menu);
                    break;
                    case PermissionConstants.PERMISSION_POINT:
                        //type == 2，按钮资源
                        PermissionPoint point = new PermissionPoint();
                        BeanUtils.populate(point, map);
                        point.setId(id);
                        j = pointMapper.insert(point);
                        break;
                        case PermissionConstants.PERMISSION_API:
                            //type == 3，api资源
                            PermissionApi api = new PermissionApi();
                            BeanUtils.populate(api, map);
                            api.setId(id);
                            j = apiMapper.insert(api);
                            break;
                default: throw new Exception();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i + j == 2;
    }

    @Override
    public boolean update(Map<String, Object> map, String id) {
        //返回结果
        int i = 0, j = 0;
        try {
            //创建权限
            Permission perm = new Permission();
            BeanUtils.populate(perm, map);
            perm.setId(id);
            i = permissionMapper.updateById(perm);
            //资源类型
            int type = perm.getType();
            switch (type) {
                case PermissionConstants.PERMISSION_MENU:
                    //type == 1，菜单资源
                    PermissionMenu menu = new PermissionMenu();
                    BeanUtils.populate(menu, map);
                    menu.setId(id);
                    j = menuMapper.updateById(menu);
                    break;
                case PermissionConstants.PERMISSION_POINT:
                    //type == 2，按钮资源
                    PermissionPoint point = new PermissionPoint();
                    BeanUtils.populate(point, map);
                    point.setId(id);
                    j = pointMapper.updateById(point);
                    break;
                case PermissionConstants.PERMISSION_API:
                    //type == 3，api资源
                    PermissionApi api = new PermissionApi();
                    BeanUtils.populate(api, map);
                    api.setId(id);
                    j = apiMapper.updateById(api);
                    break;
                default: throw new Exception();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i + j == 2;
    }

    /**
     * 根据id删除，注：相应资源表中数据也应删除
     * @param id 权限id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        int i = 0, j = 0;
        //获取权限类型
        Permission permission = permissionMapper.selectById(id);
        int type = permission.getType();
        //删除权限表
        i = permissionMapper.deleteById(id);
        //删除相应表
        if (type == 1) j = menuMapper.deleteById(id);
        if (type == 2) j = pointMapper.deleteById(id);
        if (type == 3) j = apiMapper.deleteById(id);
        return i + j == 2;
    }

    /**
     * 查询权限列表
     * @param map 查询条件
     *                  1：type -- 权限类型
     *                        0 -- 菜单+按钮，1 -- 菜单，2 -- 按钮，3 -- api
     *                  2：enVisible -- 权限可见
     *                        null -- 所有权限，0 -- 平台管理权限，1 -- 企业内部权限
     *                  3：pid -- 父权限id
     * @return
     */
    @Override
    public List<Permission> getList(Map<String, Object> map) {
        //构造查询条件
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        if (map != null) {
            String type = (String) map.get("type");
            if ("0".equals(type)) queryWrapper.in("type", 1, 2); // 查询菜单与按钮类型权限；
            else if (type != null) queryWrapper.eq("type", Integer.valueOf(type)); //查询指定类型权限；
            //若存在指定权限范围
            if (map.get("enVisible") != null) {
                queryWrapper.eq("en_visible", map.get("enVisible"));
            }
            //若存在父权限id
            if (map.get("pid") != null && !"0".equals(map.get("pid"))) {
                queryWrapper.eq("pid", map.get("pid"));
            }
        }
        //查询
        List<Permission> permissions = permissionMapper.selectList(queryWrapper);
        return permissions;
    }
}
