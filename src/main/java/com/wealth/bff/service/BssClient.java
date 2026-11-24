package com.wealth.bff.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BssClient {

    private final RestClient restClient;

    public BssClient(
            RestClient.Builder builder,
            @Value("${wealth.bss.base-url:http://localhost:8081}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public String getSampleMessage() {
        return restClient.get()
                .uri("/internal/sample")
                .retrieve()
                .body(String.class);
    }
}
