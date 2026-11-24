package com.wealth.bff.api;

import com.wealth.bff.service.BssClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SampleController {

    private final BssClient bssClient;

    public SampleController(BssClient bssClient) {
        this.bssClient = bssClient;
    }

    @GetMapping("/sample")
    public String sample() {
        return bssClient.getSampleMessage();
    }
}
