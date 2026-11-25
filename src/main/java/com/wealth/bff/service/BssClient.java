package com.wealth.bff.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        return restClient.get().uri("/wealth-service/api/v1/internal/sample").retrieve().body(String.class);
    }

    public ResponseEntity<byte[]> forward(HttpMethod method, String path, HttpHeaders headers, byte[] body) {
        RestClient.RequestBodySpec spec = restClient.method(method)
            .uri(path)
            .headers(outgoing -> outgoing.addAll(headers));
        if (body != null && body.length > 0) spec.body(body);
        return spec.exchange((request, response) -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.putAll(response.getHeaders());
            responseHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
            byte[] responseBody = response.getBody().readAllBytes();
            return new ResponseEntity<>(responseBody, responseHeaders, response.getStatusCode());
        });
    }
}
