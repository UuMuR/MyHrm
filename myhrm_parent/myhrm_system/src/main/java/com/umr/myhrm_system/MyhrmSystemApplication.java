package com.umr.myhrm_system;

import com.umr.myhrm_common.utils.IdWorker;
import com.umr.myhrm_common.utils.JwtUtils;
import com.umr.myhrm_feign.client.DepartmentClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * 系统微服务接口
 */
@SpringBootApplication(scanBasePackages = "com.umr")
@MapperScan(value = "com.umr.myhrm_system.mapper")
@EnableDiscoveryClient
@EnableFeignClients(clients = {DepartmentClient.class})
public class MyhrmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrmSystemApplication.class, args);
    }

    /**
     * id生成器
     * @return IdWorker
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    /**
     * jwt token生成工具
     * @return
     */
    @Bean
    public JwtUtils jwtUtils() {return new JwtUtils();}

    /**
     * 解决no session问题 (hibernate框架特有，查询延迟问题)
     * @return
     */
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
