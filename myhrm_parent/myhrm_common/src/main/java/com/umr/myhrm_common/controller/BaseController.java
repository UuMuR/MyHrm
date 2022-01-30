package com.umr.myhrm_common.controller;

import com.umr.myhrm_common.utils.JwtUtils;
import com.umr.myhrm_common_model.domain.system.response.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.PriorityQueue;

/**
 * 基础接口，完成公共操作
 */
public class BaseController {
    protected String companyId;
    protected String companyName;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String userId;
    protected Claims claims;

    private JwtUtils jwtUtils;

    /**
     * jwt方式
     */
//    @ModelAttribute // 继承BaseController的接口会先执行此注解下方法
//    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
//        this.request = request;
//        this.response = response;
//        //从claims中获取企业与用户信息
//        String token = request.getHeader("Authorization").replace("Bearer ", "");
//        Claims claims = jwtUtils.getClaims(token);
//        //Claims claims = (Claims) request.getAttribute("user_claims");
//        if (claims != null) {
//            this.claims = claims;
//            this.companyId = (String) claims.get("companyId");
//            this.companyName = (String) claims.get("companyName");
//            this.userId = claims.getId();
//        }
//    }

    /**
     * shiro方式
     */
    @ModelAttribute // 继承BaseController的接口会先执行此注解下方法
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        //获取安全数据
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        if (principals != null && !principals.isEmpty()) {
            ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
            Object primaryPrincipal = principals.getPrimaryPrincipal();
            try {
                BeanUtils.copyProperties(result, primaryPrincipal);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            this.userId = result.getUserId();
            this.companyName = result.getCompany();
            this.companyId = result.getCompanyId();
        }
    }
}
