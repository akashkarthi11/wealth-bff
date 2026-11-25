package com.wealth.bff.api;

import com.wealth.bff.service.BssClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.Set;

@RestController
public class WealthApiProxyController {
    private static final String BFF_PREFIX = "/wealth/api/v1";
    private static final Set<String> HOP_BY_HOP = Set.of(
        "connection", "keep-alive", "proxy-authenticate", "proxy-authorization",
        "te", "trailer", "transfer-encoding", "upgrade", "host", "content-length"
    );

    private final BssClient bssClient;

    public WealthApiProxyController(BssClient bssClient) {
        this.bssClient = bssClient;
    }

    @RequestMapping(BFF_PREFIX + "/**")
    public ResponseEntity<byte[]> forward(HttpServletRequest request) throws Exception {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String prefix = contextPath + BFF_PREFIX;
        if (!requestUri.startsWith(prefix)) {
            return ResponseEntity.notFound().build();
        }

        String suffix = requestUri.substring(prefix.length());
        String target = "/wealth-service/api/v1" + suffix;
        if (request.getQueryString() != null && !request.getQueryString().isBlank()) {
            target += "?" + request.getQueryString();
        }

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String name = names.nextElement();
            if (HOP_BY_HOP.contains(name.toLowerCase())) continue;
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements()) headers.add(name, values.nextElement());
        }

        byte[] body = request.getInputStream().readAllBytes();
        try {
            return bssClient.forward(HttpMethod.valueOf(request.getMethod()), target, headers, body);
        } catch (org.springframework.web.client.RestClientException ex) {
            return ResponseEntity.status(502)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(("{\"error\":\"Wealth business service is unavailable\",\"detail\":\""
                    + String.valueOf(ex.getMessage()).replace("\\", "\\\\").replace("\"", "\\\"")
                    + "\"}").getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }
}
