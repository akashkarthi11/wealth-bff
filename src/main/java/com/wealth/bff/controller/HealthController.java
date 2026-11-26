package com.wealth.bff.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfHealthContract;
import com.wealth.common.logger.KarthiLogger;

@RestController
public class HealthController implements IfHealthContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(HealthController.class);

	private static final String UP = "UP";
	private static final String DOWN = "DOWN";

	private final WealthFeignClient feignClient;

	public HealthController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<Map<String, String>> health() {
		Map<String, String> result = new LinkedHashMap<>();
		result.put("bff", UP);
		try {
			Map<String, Object> bss = feignClient.health();
			result.put("bss", statusOf(bss));
			Object components = bss == null ? null : bss.get("components");
			Map<?, ?> parts = components instanceof Map<?, ?> map ? map : Map.of();
			result.put("db", statusOf(parts.get("db")));
			result.put("redis", statusOf(parts.get("redis")));
		} catch (Exception e) {
			LOGGER.error("BSS health call failed");
			result.put("bss", DOWN);
			result.put("db", "UNKNOWN");
			result.put("redis", "UNKNOWN");
		}
		return ResponseEntity.ok(result);
	}

	private static String statusOf(Object node) {
		if (node instanceof Map<?, ?> map && map.get("status") != null) {
			return String.valueOf(map.get("status"));
		}
		return "UNKNOWN";
	}
}
