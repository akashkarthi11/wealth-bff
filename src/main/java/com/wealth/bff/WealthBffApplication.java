package com.wealth.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

// The BFF has no database of its own.
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,
        scanBasePackages = {"com.wealth.bff", "com.wealth.common"})
@EnableFeignClients(basePackages = "com.wealth.bff.client")
public class WealthBffApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthBffApplication.class, args);
    }
}
