package com.wealth.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.wealth.bff", "com.wealth.common"})
@EnableFeignClients(basePackages = "com.wealth.bff.common.client")
public class WealthBffApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthBffApplication.class, args);
    }
}
