package com.umr.myhrm_system.shiro.realm;

import com.umr.myhrm_common.shiro.realm.MyhrmRealm;
import com.umr.myhrm_common_model.domain.system.User;
import com.umr.myhrm_common_model.domain.system.response.ProfileResult;
import com.umr.myhrm_system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 继承自定义realm
 * 使用通用授权方法
 * 自定义用户登录认证方法
 */
public class UserRealm extends MyhrmRealm {

    @Autowired
    UserService userService;

    /**
     * login认证方法
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取登录的用户名密码（token）
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据用户名查询数据库
        User user = userService.login(mobile);
        //3.判断用户是否存在或者密码是否一致
        if (user != null && user.getPassword().equals(password)) {
            //4.如果一致返回安全数据，安全数据类型为profile
            ProfileResult profile = new ProfileResult(user);
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info =
                    new SimpleAuthenticationInfo(profile, user.getPassword(), this.getName());
            return info;
        }
        //5.不一致，返回null（抛出异常）
        return null;
    }
}
