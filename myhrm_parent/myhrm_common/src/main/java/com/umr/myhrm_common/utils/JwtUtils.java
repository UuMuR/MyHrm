package com.umr.myhrm_common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 *      主要用于权限认证token字符串的生成与解析
 */
@Getter
@Setter
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    /**
     * 签名私钥
     */
    private String key;
    /**
     * time to live
     */
    private long ttl;

    /**
     * 生成token
     * @param id 用户id
     * @param name 用户名
     * @param map 其他参数
     * @return
     */
    public String createToken(String id, String name, Map<String, Object> map) {
        //得到失效时间
        long now = System.currentTimeMillis();
        long exp = now + ttl;
        //获得token
        JwtBuilder builder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)
                .setId(id) //用户id
                .setSubject(name) //用户名
                .setExpiration(new Date(exp));
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }
        String token = builder.compact();
        return token;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims getClaims(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
