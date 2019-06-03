package com.example.captchademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//正常生产请删掉exclude= {DataSourceAutoConfiguration.class}
//swagger测试地址http://127.0.0.1/swagger-ui.html
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class}) //目前该demo完全脱离数据库 所以去除springBoot自动注入数据源
public class CaptchaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaptchaDemoApplication.class, args);
    }

}
