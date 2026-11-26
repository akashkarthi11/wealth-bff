package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.BssEndpoint;
import com.wealth.bff.client.BssHealthProbe;
import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.ApiResponseTemplate;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;
import com.wealth.common.model.dto.customer.UserDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class ConnectionsControllerTest {

	private WealthFeignClient feign;
	private BssEndpoint endpoint;
	private BssHealthProbe probe;
	private ConnectionsController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		endpoint = new BssEndpoint("http://localhost:8081");
		probe = mock(BssHealthProbe.class);
		controller = new ConnectionsController(feign, endpoint, probe, new ApiResponseTemplate());
		RequestHeaderUtil.setIp("203.0.113.5");
		signedInAs("admin");
		tableAccepts();
	}

	private void tableAccepts() {
		ApiResponse<ConnectionDto, Void> ok = new ApiResponse<>();
		ok.setStatus(1);
		when(feign.environmentAddressSave(org.mockito.ArgumentMatchers.eq("Bearer t"), org.mockito.ArgumentMatchers.eq("203.0.113.5"), org.mockito.ArgumentMatchers.any()))
				.thenReturn(ResponseEntity.ok(ok));
	}

	@Test
	void aChangeIsSavedToTheTableBeforeItIsUsedAndAResetSavesTheDeployedAddress() {
		when(probe.isUp("http://172.27.99.226:8081")).thenReturn(true);
		controller.bssUpdate("Bearer t", request("http://172.27.99.226:8081"));
		org.mockito.Mockito.verify(feign).environmentAddressSave(org.mockito.ArgumentMatchers.eq("Bearer t"), org.mockito.ArgumentMatchers.eq("203.0.113.5"),
				org.mockito.ArgumentMatchers.argThat(r -> "bff_to_bss".equals(r.getKey()) && "http://172.27.99.226:8081".equals(r.getUrl())));

		controller.bssReset("Bearer t");
		org.mockito.Mockito.verify(feign).environmentAddressSave(org.mockito.ArgumentMatchers.eq("Bearer t"), org.mockito.ArgumentMatchers.eq("203.0.113.5"),
				org.mockito.ArgumentMatchers.argThat(r -> "bff_to_bss".equals(r.getKey()) && "http://localhost:8081".equals(r.getUrl())));
	}

	@Test
	void ifTheTableRefusesTheAddressIsNotUsed() {
		when(probe.isUp("http://172.27.99.226:8081")).thenReturn(true);
		ApiResponse<ConnectionDto, Void> refused = new ApiResponse<>();
		refused.setStatus(0);
		refused.setErrorCode("6010");
		when(feign.environmentAddressSave(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
				.thenReturn(ResponseEntity.ok(refused));

		assertEquals("6010", codeOf(() -> controller.bssUpdate("Bearer t", request("http://172.27.99.226:8081"))));
		assertFalse(endpoint.isCustomised());
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private void signedInAs(String role) {
		UserDto user = new UserDto();
		user.setRole(role);
		ApiResponse<UserDto, Void> body = new ApiResponse<>();
		body.setData(user);
		when(feign.me("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));
	}

	private ConnectionRequestDto request(String url) {
		ConnectionRequestDto r = new ConnectionRequestDto();
		r.setUrl(url);
		return r;
	}

	private String codeOf(Runnable call) {
		return assertThrows(KarthiException.class, call::run).getMessage();
	}

	@Test
	void everyActionIsForAnAdminOnly() {
		signedInAs("user");
		assertEquals(BaseError.ADMIN_ONLY_ACTION, codeOf(() -> controller.bssGet("Bearer t")));
		assertEquals(BaseError.ADMIN_ONLY_ACTION, codeOf(() -> controller.bssUpdate("Bearer t", request("http://172.27.99.226:8081"))));
		assertEquals(BaseError.ADMIN_ONLY_ACTION, codeOf(() -> controller.bssReset("Bearer t")));
		assertFalse(endpoint.isCustomised());
	}

	@Test
	void getShowsTheCurrentAddressTheDefaultAndTheLiveStatus() {
		when(probe.isUp("http://localhost:8081")).thenReturn(true);
		ConnectionDto dto = controller.bssGet("Bearer t").getBody().getData();
		assertEquals("http://localhost:8081", dto.getUrl());
		assertEquals("http://localhost:8081", dto.getDefaultUrl());
		assertFalse(dto.isCustomised());
		assertEquals("UP", dto.getStatus());
	}

	@Test
	void anAddressIsSavedOnlyWhenItAnswersUp() {
		when(probe.isUp("http://172.27.99.226:8081")).thenReturn(false);
		assertEquals(BaseError.CONNECTION_NOT_UP, codeOf(() -> controller.bssUpdate("Bearer t", request("http://172.27.99.226:8081"))));
		assertEquals("http://localhost:8081", endpoint.current());

		when(probe.isUp("http://172.27.99.226:8081")).thenReturn(true);
		ConnectionDto dto = controller.bssUpdate("Bearer t", request("http://172.27.99.226:8081/")).getBody().getData();
		assertEquals("http://172.27.99.226:8081", dto.getUrl());
		assertTrue(dto.isCustomised());
	}

	@Test
	void aBadAddressIsRefusedBeforeAnythingIsCalled() {
		assertEquals(BaseError.CONNECTION_ADDRESS_INVALID, codeOf(() -> controller.bssUpdate("Bearer t", request("ftp://x"))));
		assertFalse(endpoint.isCustomised());
	}

	@Test
	void resetGoesBackToTheDeployedAddress() {
		endpoint.set("http://172.27.99.226:8081");
		ConnectionDto dto = controller.bssReset("Bearer t").getBody().getData();
		assertEquals("http://localhost:8081", dto.getUrl());
		assertFalse(dto.isCustomised());
	}
}
