package com.umr.myhrm_employee;

import com.umr.myhrm_employee.service.UserCompanyPersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@SpringBootTest
class MyhrmEmployeeApplicationTests {

    @Autowired
    UserCompanyPersonService service;

    @Test
    void contextLoads() throws IOException {
        String s = null;
        System.out.println(s.toString());
    }
}