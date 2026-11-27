package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.IndstocksSaveRequestDto;
import com.wealth.common.model.dto.admin.IndstocksStatusDto;

class IndstocksControllerTest {

	private WealthFeignClient feign;
	private IndstocksController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new IndstocksController(feign);
	}

	private IndstocksSaveRequestDto token(String value) {
		IndstocksSaveRequestDto request = new IndstocksSaveRequestDto();
		request.setToken(value);
		return request;
	}

	@Test
	void everyCallIsForwardedWithTheTokenAndTheApiTokenIsCheckedAtTheEdge() {
		ApiResponse<IndstocksStatusDto, Void> status = new ApiResponse<>();
		when(feign.indstocksStatus(any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.indstocksSave(any(), any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.indstocksTest(any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.indstocksRemove(any(), any())).thenReturn(ResponseEntity.ok(new ApiResponse<>()));

		assertEquals(status, controller.status("Bearer t").getBody());
		assertEquals(status, controller.test("Bearer t").getBody());
		controller.remove("Bearer t");
		assertEquals(status, controller.save("Bearer t", token("abc.def-123")).getBody());

		for (String bad : new String[] { null, "", "   ", "x".repeat(501), "a\nb" }) {
			assertEquals(BaseError.PROVIDER_INVALID, assertThrows(KarthiException.class, () -> controller.save("Bearer t", token(bad))).getMessage());
		}
		assertThrows(KarthiException.class, () -> controller.save("Bearer t", null));
		verify(feign, times(1)).indstocksSave(any(), any(), any());
	}
}
