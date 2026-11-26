package com.wealth.bff.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.constants.WealthConstants;
import com.wealth.bff.contract.IfHealthContract;
import com.wealth.common.logger.KarthiLogger;

@RestController
public class HealthController implements IfHealthContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(HealthController.class);

	private final WealthFeignClient feignClient;

	public HealthController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<Map<String, String>> health() {
		Map<String, String> result = new LinkedHashMap<>();
		result.put(WealthConstants.KEY_BFF, WealthConstants.STATUS_UP);
		try {
			Map<String, Object> bss = feignClient.health();
			result.put(WealthConstants.KEY_BSS, statusOf(bss));
			Object components = bss == null ? null : bss.get(WealthConstants.KEY_COMPONENTS);
			Map<?, ?> parts = components instanceof Map<?, ?> map ? map : Map.of();
			result.put(WealthConstants.KEY_DB, statusOf(parts.get(WealthConstants.KEY_DB)));
			result.put(WealthConstants.KEY_REDIS, statusOf(parts.get(WealthConstants.KEY_REDIS)));
		} catch (Exception e) {
			LOGGER.error("BSS health call failed");
			result.put(WealthConstants.KEY_BSS, WealthConstants.STATUS_DOWN);
			result.put(WealthConstants.KEY_DB, WealthConstants.STATUS_UNKNOWN);
			result.put(WealthConstants.KEY_REDIS, WealthConstants.STATUS_UNKNOWN);
		}
		return ResponseEntity.ok(result);
	}

	private static String statusOf(Object node) {
		if (node instanceof Map<?, ?> map && map.get(WealthConstants.KEY_STATUS) != null) {
			return String.valueOf(map.get(WealthConstants.KEY_STATUS));
		}
		return WealthConstants.STATUS_UNKNOWN;
	}
}
