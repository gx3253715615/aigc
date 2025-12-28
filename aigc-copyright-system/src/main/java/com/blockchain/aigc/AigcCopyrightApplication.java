package com.blockchain.aigc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.blockchain.aigc.mapper")
public class AigcCopyrightApplication {
    public static void main(String[] args) {
        SpringApplication.run(AigcCopyrightApplication.class, args);
    }
}
