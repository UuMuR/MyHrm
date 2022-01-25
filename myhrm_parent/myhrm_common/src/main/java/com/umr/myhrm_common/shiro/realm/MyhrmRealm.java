package com.umr.myhrm_common.shiro.realm;

import com.umr.myhrm_common_model.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import java.util.Set;

/**
 * shiro公共realm域
 * 定义通用授权方法
 */
public class MyhrmRealm extends AuthorizingRealm {

    public void setName(String name) {super.setName("myhrmRealm");}

    /**
     * 授权方法
      * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取安全数据
        ProfileResult profile = (ProfileResult) principalCollection.getPrimaryPrincipal();
        //2.根据用户数据获取用户的权限信息（api权限）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> apis = (Set<String>) profile.getRoles().get("apis");
        info.setStringPermissions(apis);
        return info;
    }

    /**
     * 认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
