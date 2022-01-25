package com.umr.myhrm_employee;

import com.umr.myhrm_common.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.umr")
@MapperScan(value = "com.umr.myhrm_employee.mapper")
@EnableDiscoveryClient
public class MyhrmEmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyhrmEmployeeApplication.class, args);
    }

    /**
     * id生成器
     * @return IdWorker
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}
