package com.bingshan.account1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class Account1Application {

    public static void main(String[] args) {
        SpringApplication.run(Account1Application.class, args);
    }

}
