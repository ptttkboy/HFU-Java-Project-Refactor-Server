package com.hfu.project_server;

import com.hfu.project_server.security.member_web_api.jwt.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectServerApplicationTests {

    @Autowired
    private JwtProperties jwtProperties;
    @Test
    void contextLoads() {
        System.out.println(jwtProperties.toString());
    }

}
