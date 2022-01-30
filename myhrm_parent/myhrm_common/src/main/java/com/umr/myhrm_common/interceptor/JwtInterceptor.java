package com.umr.myhrm_common.interceptor;

import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt鉴权拦截器
 *  --1、识别用户是否登录
 *  --2、识别是否有访问的api的使用权限
 */
//@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {
            //转换请求头为token
            String token = authorization.replace("Bearer ", "");
            //解析token获取id
            Claims claims = jwtUtils.getClaims(token);
            if (!claims.isEmpty()) {
                //校验是否有访问该api的权限
                String apis = (String) claims.get("apis");
                HandlerMethod method = (HandlerMethod) handler;
                RequestMapping annotation = method.getMethodAnnotation(RequestMapping.class);
                String name = annotation.name();
                //若访问api接口没有指定注解属性name，则没有限制访问，可以直接放行
                if (StringUtils.isEmpty(name) || (!StringUtils.isEmpty(apis) && apis.contains(name))){
                    //将claims存入请求域对象中
                    request.setAttribute("user_claims", claims);
                    return true;
                } else
                    throw new NoResultException(ResultCode.UNAUTHORISE);
            }
        }
        //无此header表示尚未登录
        throw new NoResultException(ResultCode.UNAUTHENTICATED);
    }
}
