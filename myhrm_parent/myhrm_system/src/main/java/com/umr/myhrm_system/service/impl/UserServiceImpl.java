package com.umr.myhrm_system.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common_model.domain.system.Permission;
import com.umr.myhrm_common_model.domain.system.Role;
import com.umr.myhrm_common_model.domain.system.response.UserResult;
import com.umr.myhrm_common_model.domain.system.User;
import com.umr.myhrm_common_model.domain.system.User_Role;
import com.umr.myhrm_system.mapper.UserMapper;
import com.umr.myhrm_system.mapper.User_RoleMapper;
import com.umr.myhrm_system.service.PermissionService;
import com.umr.myhrm_system.service.RoleService;
import com.umr.myhrm_system.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 用户表业务逻辑
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    User_RoleMapper user_roleMapper;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;

    /**
     * 绑定用户 -- 角色关系
     * @param userId 用户id
     * @param roleIds 用户所有角色集合
     * @return
     */
    @Override
    public boolean saveRole(String userId, List<String> roleIds) {
        //先删除该用户的所有 角色--用户 映射关系
        user_roleMapper.delete(new QueryWrapper<User_Role>().eq("user_id", userId));
        //为该用户添加角色
        for (String roleId : roleIds) {
            user_roleMapper.insert(new User_Role(userId, roleId));
        }
        return true;
    }

    /**
     * 保存用户细腻些
     * @param user
     * @return
     */
    @Override
    public boolean saveOne(User user) {
        //生成id
        user.setId(String.valueOf(idWorker.nextId()));
        //未设置密码，默认密码 123456
        if (user.getPassword() == null || user.getPassword().length() == 0)
            user.setPassword("123456");
        //加密密码
        user.setPassword(new Md5Hash(user.getPassword(), user.getMobile(), 3).toString());
        //默认状态码
        user.setEnableState(1);
        //默认用户等级为user
        user.setLevel("user");
        //保存
        int i = userMapper.insert(user);
        return i == 1;
    }

    /**
     * 获取用户列表
     * @param companyId 企业id
     * @param queryMap 查询信息
     *                 -- 部门id
     *                 -- 是否分配
     * @param page 分页信息
     * @param size
     * @return
     */
    @Override
    public IPage<User> getList(String companyId, Map<String, Object> queryMap, int page, int size) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);
        if (!StringUtils.isEmpty((String)queryMap.get("departmentId")))
            queryWrapper.eq("department_id", queryMap.get("departmentId"));
        if ("0".equals((String)queryMap.get("hasDept")))
            queryWrapper.isNull("departmentId");
        IPage<User> queryPage = new Page<>(page, size);
        IPage<User> userIPage = userMapper.selectPage(queryPage, queryWrapper);
        return userIPage;
    }

    /**
     * 根据id查找用户
     * 并绑定用户所有的角色id
     * @param id
     * @return
     */
    @Override
    public UserResult getOne(String id) {
        List<User_Role> user_roles = user_roleMapper
                .selectList(new QueryWrapper<User_Role>()
                        .eq("user_id", id));
        List<String> roleIds = new ArrayList<>();
        //为用户绑定所有角色与角色id
        for (User_Role user_role : user_roles) {
            roleIds.add(user_role.getRoleId());
        }
        //调用getUser方法绑定
        User user = userMapper.selectById(id);
        return new UserResult(user, roleIds);
    }

    /**
     * 用户登录
     * @param mobile 用户手机号
     * @return
     */
    @Override
    public User login(String mobile) {
        User user = this.getUser(userMapper
                .selectOne(new QueryWrapper<User>().eq("mobile", mobile))
                .getId());
        return user;
    }

    /**
     * 上传用户头像
     * @param id  用户id
     * @param img  图片DatUrl
     * @return  回显图片的请求url
     */
    @Override
    public String upload(String id, MultipartFile img) throws IOException {
        User user = userMapper.selectById(id);
        //Base64解码获得图片字节码数组
        String encode = "data:image/jpg;base64," + Base64.encode(img.getBytes());
        user.setStaffPhoto(encode);
        userMapper.updateById(user);
        return encode;
    }

    /**
     * 获取用户与其具体角色，角色包含其具体权限
     * @param id
     *      需要判断用户权限等级level
     *      saasAdmin -- 平台管理员，查询所有权限
     *      coAdmin -- 企业管理员，查询所有企业可见权限
     *      user -- 普通用户，查询其分配到的权限
     * @return
     */
    @Override
    public User getUser(String id) {
        User user = userMapper.selectById(id);
        Set<Role> roles = new HashSet<>();
        //平台管理员，为其添加一个拥有所有权限的角色
        if ("saasAdmin".equals(user.getLevel())) {
            Set<Permission> allPerm = new HashSet<>(permissionService.getList(null));
            Role role = new Role();
            role.setName("saasAdmin");
            role.setPermissions(allPerm);
            roles.add(role);
        }
        //企业管理员，为其添加一个角色，能访问所有enVisible为1的权限
        if ("coAdmin".equals(user.getLevel())) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("enVisible", 1);
            HashSet<Permission> coPerm = new HashSet<>(permissionService.getList(queryMap));
            Role role = new Role();
            role.setName("coAdmin");
            role.setPermissions(coPerm);
            roles.add(role);
        }
        //普通用户，为其添加所有角色
        if ("user".equals(user.getLevel())) {
            List<User_Role> user_roles = user_roleMapper
                    .selectList(new QueryWrapper<User_Role>()
                            .eq("user_id", id));
            for (User_Role user_role : user_roles) {
                Role role = roleService.getRole(user_role.getRoleId());
                roles.add(role);
            }
        }
        user.setRoles(roles);
        return user;
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public boolean deleteOne(String id) {
        //先删除用户所有角色
        user_roleMapper.delete(new QueryWrapper<User_Role>().eq("user_id", id));
        //删除用户信息
        int i = userMapper.deleteById(id);
        return i == 1;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public boolean updateOne(User user) {
        int i = userMapper.update(user, new QueryWrapper<User>().eq("id", user.getId()));
        return i == 1;
    }
}
