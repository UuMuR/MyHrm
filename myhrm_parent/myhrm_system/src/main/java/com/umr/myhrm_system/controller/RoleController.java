package com.umr.myhrm_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.umr.myhrm_common.controller.BaseController;
import com.umr.myhrm_common.entity.PageResult;
import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.response.RoleResult;
import com.umr.myhrm_feign.client.DepartmentClient;
import com.umr.myhrm_system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin // 解决跨域问题
@RestController
@RequestMapping("/sys")
public class RoleController extends BaseController {
    @Autowired
    RoleService roleService;
    @Autowired
    DepartmentClient departmentClient;

    @PutMapping("/role/assignPrem")
    public Result assignRoles(@RequestBody Map map) {
        boolean flag = roleService.savePermission((String) map.get("id"), (List<String>) map.get("permIds"));
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    @PostMapping("/role")
    public Result save(@RequestBody Role role) {
        role.setCompanyId(companyId);
        boolean flag = roleService.save(role);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }


    @PutMapping("/role/{id}")
    public Result update(@PathVariable String id, @RequestBody Role role) {
        role.setId(id);
        boolean flag = roleService.updateById(role);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }


    @DeleteMapping("/role/{id}")
    public Result delete(@PathVariable String id) {
        boolean flag = roleService.removeById(id);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }


    @GetMapping("/role/{id}")
    public Result getById(@PathVariable String id) throws NoResultException {
        RoleResult role = roleService.getOne(id);
        if (role == null) {
            throw new NoResultException(ResultCode.NONE_RESULT);
        }
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(role);
        return result;
    }

    @GetMapping("/role")
    public Result getList(@RequestParam int page, int pagesize) {
        IPage<Role> roleIPage = roleService.getPage(companyId, page, pagesize);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(new PageResult<Role>(roleIPage.getTotal(), roleIPage.getRecords()));
        return result;
    }

    @GetMapping("/role/list")
    public Result list() {
        List<Role> roles = roleService
                .list(new QueryWrapper<Role>()
                .eq("company_id", companyId));
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(roles);
        return result;
    }
}
