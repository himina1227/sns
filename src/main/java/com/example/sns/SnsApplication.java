package com.example.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Jpa를 설정하게 되면 자동으로 DataSourceAutoConfiguration 설정 정보를 읽게된다.
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnsApplication.class, args);
    }

}
