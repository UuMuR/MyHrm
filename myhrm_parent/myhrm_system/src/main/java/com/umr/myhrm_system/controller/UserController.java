package com.umr.myhrm_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.umr.myhrm_common.controller.BaseController;
import com.umr.myhrm_common.entity.PageResult;
import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common.utils.JwtUtils;
import com.umr.myhrm_common.utils.PermissionConstants;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.response.ProfileResult;
import com.umr.myhrm_common_model.domain.system.response.UserResult;
import com.umr.myhrm_common_model.domain.system.User;
import com.umr.myhrm_common_model.domain.system.response.UserSimpleResult;
import com.umr.myhrm_feign.client.DepartmentClient;
import com.umr.myhrm_system.service.PermissionService;
import com.umr.myhrm_system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户crud控制器
 */
@CrossOrigin // 解决跨域问题
@RestController
@RequestMapping("/sys")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    PermissionService permissionService;
    @Autowired
    DepartmentClient departmentClient;

    /**
     * 上传用户头像
     *
     * @param id   用户id
     * @param file 图片
     * @return
     */
    @PostMapping("/user/upload/{id}")
    public Result upload(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        String img = userService.upload(id, file);
        return new Result(ResultCode.SUCCESS, img);
    }

    /**
     * 导入excel表数据进入user
     *
     * @param file 文件为excel表，文件名为‘file’
     * @return
     */
    @PostMapping("/user/import")
    public Result importUser(@RequestParam("file") MultipartFile file) throws Exception {
        //获取excel表
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        for (int i = 1; i <= lastRow; ++i) {
            Row row = sheet.getRow(i);
            Object[] values = new Object[row.getLastCellNum()];
            for (int j = 1; j < row.getLastCellNum(); ++j) {
                Cell cell = row.getCell(j);
                values[j] = getValue(cell);
            }
            User user = new User(values);
            //调用部门服务 -- 通过code查询部门
            user.setDepartmentId(departmentClient.getByCode(user.getDepartmentId(), companyId).getId());
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            userService.saveOne(user);
        }
        return new Result(ResultCode.SUCCESS);
    }

    //获取数据
    private static Object getValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case STRING: //字符串类型
                value = cell.getStringCellValue();
                break;
            case BOOLEAN: //boolean类型
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC: //数字类型（包含日期和普通数字）
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //公式类型
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 用户登录
     *
     * @param map 包含手机号与密码
     *            "mobile" -- 手机号
     *            "password" -- 密码
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> map) {
        String mobile = map.get("mobile");
        String password = map.get("password");
        /**
         * 基于shiro签发token
         */
        try {
            //加密密码
            password = new Md5Hash(password, mobile, 3).toString();
            //构造username&password令牌
            UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //进行login验证
            subject.login(upToken);
            //获取sessionId
            String sessionId = (String) subject.getSession().getId();
            return new Result(ResultCode.SUCCESS, sessionId);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new Result(ResultCode.LOGIN_ERROR);
        }
        /**
         * 基于jwt签发token
         */
        //加密密码
        //password = new Md5Hash(password, mobile, 3).toString();
//        User user = userService.login(mobile);
//        //比较密码是否一致
//        if (user != null && user.getPassword().equals(password)) {
//            //为token添加api权限字符串
//            StringBuilder builder = new StringBuilder();
//            if (user.getRoles() != null && user.getRoles().size() != 0) {
//                for (Role role : user.getRoles()) {
//                    for (Permission perm : role.getPermissions()) {
//                        if (perm.getType() == PermissionConstants.PERMISSION_API) {
//                            builder.append(perm.getCode()).append(",");
//                        }
//                    }
//                }
//            }
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("apis", builder.toString());
//            userMap.put("companyId", user.getCompanyId());
//            userMap.put("companyName", user.getCompanyName());
//            //生成token
//            String token = jwtUtils.createToken(user.getId(), user.getUsername(), userMap);
//            return new Result(ResultCode.SUCCESS, token);
//        } else {
//            //密码错误 或 用户不存在
//            return new Result(ResultCode.LOGIN_ERROR);
//        }
    }

    /**
     * 为用户分配角色
     *
     * @param map “id” -- 用户id
     *            “roleIds” -- 要绑定的角色列表
     * @return
     */
    @PutMapping("/user/assignRoles")
    public Result assignRoles(@RequestBody Map map) {
        boolean flag = userService
                .saveRole((String) map.get("id"), (List<String>) map.get("roleIds"));
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/user")
    public Result save(@RequestBody User user) {
        user.setCompanyId(companyId);
        boolean flag = userService.saveOne(user);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 更新用户信息
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/user/{id}")
    public Result update(@PathVariable String id, @RequestBody User user) {
        user.setId(id);
        boolean flag = userService.updateOne(user);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 删除用户信息
     *
     * @param id
     * @return
     */
    @RequiresPermissions("api-user-delete")
    @DeleteMapping("/user/{id}")
    public Result delete(@PathVariable String id) {
        boolean flag = userService.deleteOne(id);
        if (flag) {
            return new Result(ResultCode.SUCCESS);
        }
        return new Result(ResultCode.FAIL);
    }

    /**
     * 查询用户
     *
     * @param id
     * @return UserResult 对象，包含用户基本信息与所有角色
     * @throws NoResultException
     */
    @GetMapping("/user/{id}")
    public Result getById(@PathVariable String id) throws NoResultException {
        UserResult user = userService.getOne(id);
        if (user == null) {
            throw new NoResultException(ResultCode.NONE_RESULT);
        }
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(user);
        return result;
    }

    /**
     * 获取当前企业所有用户简洁信息
     *
     * @return
     */
    @GetMapping("/user/simple")
    public Result simple() {
        List<User> users = userService
                .list(new QueryWrapper<User>().eq("company_id", companyId));
        List<UserSimpleResult> simpleResults = new ArrayList<>();
        for (User user : users) {
            simpleResults.add(new UserSimpleResult(user.getId(), user.getUsername()));
        }
        return new Result(ResultCode.SUCCESS, simpleResults);
    }

    /**
     * 查询用户列表
     *
     * @param page
     * @param size
     * @param map
     * @return
     */
    @GetMapping("/user")
    public Result getList(int page, int size, @RequestParam Map map) throws NoResultException {
        Result result = new Result();
        if (!StringUtils.isEmpty(companyId)) {
            IPage<User> userIPage = userService.getList(companyId, map, page, size);
            result = new Result(ResultCode.SUCCESS);
            result.setData(new PageResult<>(userIPage.getTotal(), userIPage.getRecords()));
        } else {
            //平台管理员登陆时，应返回所有用户列表显示在前端
            IPage<User> userPage = userService.page(new Page<>(page, size));
            result = new Result(ResultCode.SUCCESS);
            result.setData(new PageResult<>(userPage.getTotal(), userPage.getRecords()));
        }
        return result;
    }

    /**
     * 用户登陆成功后获取用户信息
     */
    @PostMapping("/profile")
    public Result getById() {
        /**
         * jwt方式根据claims获取用户信息
         */
        //User user = userService.getUser(claims.getId());
        /**
         * shiro方式根据subject获取用户信息
         */
        //从session中获取安全数据
        Subject subject = SecurityUtils.getSubject();
        ProfileResult result = (ProfileResult) subject.getPrincipals().getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);
    }
}
