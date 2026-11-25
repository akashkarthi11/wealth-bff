package com.wealth.bff.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;

@Component
public class BssClient {
    private final RestClient restClient;

    public BssClient(
            RestClient.Builder builder,
            @Value("${wealth.bss.base-url:http://localhost:8081}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public String getSampleMessage() {
        return restClient.get().uri("/internal/sample").retrieve().body(String.class);
    }

    public ResponseEntity<byte[]> forward(HttpMethod method, URI path, HttpHeaders headers, byte[] body) {
        return restClient.method(method)
            .uri(path)
            .headers(outgoing -> outgoing.addAll(headers))
            .body(body == null ? new byte[0] : body)
            .exchange((request, response) -> {
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.putAll(response.getHeaders());
                byte[] responseBody = response.getBody().readAllBytes();
                return new ResponseEntity<>(responseBody, responseHeaders, response.getStatusCode());
            });
    }
}
