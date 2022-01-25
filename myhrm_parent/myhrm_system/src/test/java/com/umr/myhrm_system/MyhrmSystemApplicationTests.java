package com.umr.myhrm_system;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyhrmSystemApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Md5Hash("000000", "111", 3).toString());
    }


}
