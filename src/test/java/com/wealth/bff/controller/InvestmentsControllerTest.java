package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.invest.InvestmentsDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class InvestmentsControllerTest {

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	@Test
	void theListIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<InvestmentsDto, Void> body = new ApiResponse<>();
		when(feign.investmentsList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new InvestmentsController(feign).list("Bearer t").getBody());
	}

	@Test
	void theRefreshButtonIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<InvestmentsDto, Void> body = new ApiResponse<>();
		when(feign.investmentsRefresh("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new InvestmentsController(feign).refresh("Bearer t").getBody());
	}

	@Test
	void aHoldingIsForwardedWithTheTokenAndABadShapeNeverReachesTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		com.wealth.common.model.dto.invest.HoldingSaveRequestDto holding = new com.wealth.common.model.dto.invest.HoldingSaveRequestDto();
		holding.setAccountId("11111111-2222-3333-4444-555555555555");
		holding.setAssetClass("precious_metal");
		holding.setName("Gold");
		holding.setUnit("g");
		holding.setQuantity(new java.math.BigDecimal("2"));
		ApiResponse<com.wealth.common.model.dto.invest.HoldingDto, Void> body = new ApiResponse<>();
		when(feign.investmentsSaveHolding("Bearer t", "203.0.113.5", holding)).thenReturn(ResponseEntity.ok(body));
		InvestmentsController controller = new InvestmentsController(feign);

		assertEquals(body, controller.saveHolding("Bearer t", holding).getBody());

		holding.setName(" ");
		org.junit.jupiter.api.Assertions.assertEquals(com.wealth.common.generic.BaseError.INVALID_REQUEST,
				org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.saveHolding("Bearer t", holding)).getMessage());
		holding.setName("Gold");
		holding.setAccountId("nope");
		org.junit.jupiter.api.Assertions.assertEquals(com.wealth.common.generic.BaseError.INVALID_REQUEST,
				org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.saveHolding("Bearer t", holding)).getMessage());
	}
}
