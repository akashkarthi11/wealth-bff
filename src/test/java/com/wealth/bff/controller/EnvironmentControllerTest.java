package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class EnvironmentControllerTest {

	private WealthFeignClient feign;
	private EnvironmentController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new EnvironmentController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private ConnectionRequestDto request(String address) {
		ConnectionRequestDto r = new ConnectionRequestDto();
		r.setUrl(address);
		return r;
	}

	@Test
	void readsAndResetsForwardTheTokenAndTheCallerIp() {
		ApiResponse<ConnectionDto, Void> body = new ApiResponse<>();
		when(feign.environmentRedisGet("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));
		when(feign.environmentKafkaReset("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(body));

		assertEquals(body, controller.redisGet("Bearer t").getBody());
		assertEquals(HttpStatus.ACCEPTED, controller.kafkaReset("Bearer t").getStatusCode());
	}

	@Test
	void aServiceAddressIsCheckedAtTheEdgeThenForwarded() {
		ConnectionRequestDto ok = request("http://172.27.99.226:8080");
		ok.setKey("ui_to_bff");
		ApiResponse<ConnectionDto, Void> body = new ApiResponse<>();
		when(feign.environmentAddressSave("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.addressSave("Bearer t", ok).getBody());

		for (String badUrl : new String[] { "redis.example.com:6379", "ftp://x", "http://x/path", "http://x;drop", "" }) {
			ConnectionRequestDto bad = request(badUrl);
			bad.setKey("bff_to_bss");
			assertEquals(BaseError.CONNECTION_ADDRESS_INVALID, assertThrows(KarthiException.class, () -> controller.addressSave("Bearer t", bad)).getMessage(), badUrl);
		}
		ConnectionRequestDto wrongKey = request("http://x:1");
		wrongKey.setKey("redis");
		assertThrows(KarthiException.class, () -> controller.addressSave("Bearer t", wrongKey));
	}

	@Test
	void anUpdateIsCheckedAtTheEdgeThenForwarded() {
		ConnectionRequestDto ok = request("redis.example.com:6379");
		ApiResponse<ConnectionDto, Void> body = new ApiResponse<>();
		when(feign.environmentRedisUpdate("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.redisUpdate("Bearer t", ok).getBody());
		assertEquals(BaseError.ENVIRONMENT_ADDRESS_INVALID, assertThrows(KarthiException.class, () -> controller.redisUpdate("Bearer t", request("a;b"))).getMessage());
		assertThrows(KarthiException.class, () -> controller.kafkaUpdate("Bearer t", request(" ")));
		assertThrows(KarthiException.class, () -> controller.kafkaUpdate("Bearer t", null));

		ConnectionRequestDto longPassword = request("db.example.com:5432");
		longPassword.setPassword("x".repeat(201));
		assertEquals(BaseError.ENVIRONMENT_CREDENTIALS_INVALID, assertThrows(KarthiException.class, () -> controller.postgresUpdate("Bearer t", longPassword)).getMessage());
		verify(feign, never()).environmentPostgresUpdate(any(), any(), any());

		ConnectionRequestDto login = request("db.example.com:5432");
		login.setUsername("bob");
		login.setPassword("pw");
		ApiResponse<ConnectionDto, Void> saved = new ApiResponse<>();
		when(feign.environmentPostgresUpdate("Bearer t", "203.0.113.5", login)).thenReturn(ResponseEntity.ok(saved));
		assertEquals(saved, controller.postgresUpdate("Bearer t", login).getBody());
		verify(feign, never()).environmentKafkaUpdate(any(), any(), any());
	}
}
