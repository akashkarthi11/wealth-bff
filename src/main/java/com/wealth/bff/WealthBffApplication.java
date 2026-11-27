package com.wealth.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

// The BFF has no database of its own. Scheduling is on for the API usage report to the BSS.
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,
        scanBasePackages = {"com.wealth.bff", "com.wealth.common"})
@EnableFeignClients(basePackages = "com.wealth.bff.client")
@EnableScheduling
public class WealthBffApplication {
    public static void main(String[] args) {
        SpringApplication.run(WealthBffApplication.class, args);
    }
}
