package com.umr.myhrm_gateway;

import com.umr.myhrm_common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.umr")
@EnableDiscoveryClient
public class MyhrmGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyhrmGatewayApplication.class, args);
    }

    /**
     * jwt token生成工具
     * @return
     */
    @Bean
    public JwtUtils jwtUtils() {return new JwtUtils();}
}
