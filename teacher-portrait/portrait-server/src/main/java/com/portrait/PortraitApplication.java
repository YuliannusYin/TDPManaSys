package com.portrait;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.portrait.mapper")
public class PortraitApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortraitApplication.class, args);
    }
}