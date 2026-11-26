package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;

class HealthControllerTest {

	@Test
	void reportsBssDbAndRedisFromTheBssHealth() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		when(feign.health()).thenReturn(Map.of("status", "UP",
				"components", Map.of("db", Map.of("status", "UP"), "redis", Map.of("status", "DOWN"))));

		ResponseEntity<Map<String, String>> response = new HealthController(feign).health();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Map.of("bff", "UP", "bss", "UP", "db", "UP", "redis", "DOWN"), response.getBody());
	}

	@Test
	void reportsBssDownWhenTheCallFails() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		when(feign.health()).thenThrow(new RuntimeException("connection refused"));

		ResponseEntity<Map<String, String>> response = new HealthController(feign).health();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Map.of("bff", "UP", "bss", "DOWN", "db", "UNKNOWN", "redis", "UNKNOWN"), response.getBody());
	}
}
