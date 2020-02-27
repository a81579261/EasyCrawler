package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Agus
 */
@MapperScan("com.example.mapper")
@SpringBootApplication
public class EasyCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyCrawlerApplication.class, args);
    }

}
