package com.umr.myhrm_gateway.filters;

import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.exception.NoResultException;
import com.umr.myhrm_common.utils.JwtUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Order(1)
//@Component
public class AuthorizationFilter implements GlobalFilter {

    @Autowired
    JwtUtils jwtUtils;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request信息
        ServerHttpRequest request = exchange.getRequest();
        //获取请求头
        String authorization = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {
            //转换请求头为token
            String token = authorization.replace("Bearer ", "");
            if (!StringUtils.isEmpty(token)) {
                return chain.filter(exchange);
            }
        }
        //无此header表示尚未登录
        throw new NoResultException(ResultCode.UNAUTHENTICATED);
    }
}
