package com.umr.myhrm_company;

import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common.utils.JwtUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.umr")
@MapperScan(value = "com.umr.myhrm_company.mapper")
@EnableDiscoveryClient
public class MyhrmCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrmCompanyApplication.class, args);
    }

    /**
     * 初始化 id生成器
     * @return IdWorker
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    /**
     * 初始化jwt工具类
     */
    @Bean
    public JwtUtils jwtUtils() {return new JwtUtils();}
}
