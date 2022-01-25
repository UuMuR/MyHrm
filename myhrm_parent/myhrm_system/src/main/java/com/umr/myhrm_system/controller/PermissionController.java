package com.umr.myhrm_system.controller;

import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 权限管理crud控制器
 */
@CrossOrigin // 解决跨域问题
@RestController
@RequestMapping("/sys")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    /**
     * 添加权限
     * @param map map中包含api，menu，point的所有字段
     * @return
     */
    @PostMapping("/permission")
    public Result save(@RequestBody Map<String, Object> map) {
        boolean flag = permissionService.save(map);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 更新权限
     * @param id 权限id
     * @param map
     * @return
     */
    @PutMapping("/permission/{id}")
    public Result update(@PathVariable String id, @RequestBody Map<String, Object> map) {
        boolean flag = permissionService.update(map, id);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 按id查询权限
     * @param id
     * @return
     */
    @GetMapping("/permission/{id}")
    public Result getOne(@PathVariable String id) {
        Permission perm = permissionService.getById(id);
        if (perm == null) {
            try {
                throw new NoResultException(ResultCode.NONE_RESULT);
            } catch (NoResultException e) {
                e.printStackTrace();
            }
        }
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(perm);
        return result;
    }

    /**
     * 按id删除权限
     * @param id
     * @return
     */
    @DeleteMapping("/permission/{id}")
    public Result delete(@PathVariable String id) {
        boolean flag = permissionService.deleteById(id);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 查询权限列表
     * @param map 查询条件
     *            1：type -- 权限类型
     *                  0 -- 菜单+按钮，1 -- 菜单，2 -- 按钮，3 -- api
     *            2：enVisible -- 权限可见
     *                  null -- 所有权限，0 -- 平台管理权限，1 -- 企业内部权限
     *            3：pid -- 父权限id
     * @return
     */
    @GetMapping("/permission")
    public Result getList(@RequestParam Map map) {
        List<Permission> permissions = permissionService.getList(map);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(permissions);
        return result;
    }
}
