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
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ApiUsageDto;
import com.wealth.common.model.dto.admin.ApiUsageRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class ApiUsageControllerTest {

	private WealthFeignClient feign;
	private ApiUsageController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new ApiUsageController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private ApiUsageRequestDto days(Integer days) {
		ApiUsageRequestDto request = new ApiUsageRequestDto();
		request.setDays(days);
		return request;
	}

	@Test
	void theDaysAreCheckedAtTheEdgeThenForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<ApiUsageDto, Void> body = new ApiResponse<>();
		ApiUsageRequestDto thirty = days(30);
		when(feign.apiUsageList("Bearer t", "203.0.113.5", thirty)).thenReturn(ResponseEntity.ok(body));
		when(feign.apiUsageList("Bearer t", "203.0.113.5", null)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.list("Bearer t", thirty).getBody());
		assertEquals(body, controller.list("Bearer t", null).getBody(), "no days means the BSS default");
	}

	@Test
	void aWrongNumberOfDaysNeverReachesTheBss() {
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.list("Bearer t", days(0))).getMessage());
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.list("Bearer t", days(31))).getMessage());
		verify(feign, never()).apiUsageList(any(), any(), any());
	}

	@Test
	void theUserChoiceIsEmptyMeOrAnIdAndAnythingElseNeverReachesTheBss() {
		ApiUsageRequestDto mine = days(7);
		mine.setUserId("me");
		ApiUsageRequestDto one = days(7);
		one.setUserId("11111111-1111-1111-1111-111111111111");
		ApiResponse<ApiUsageDto, Void> body = new ApiResponse<>();
		when(feign.apiUsageList("Bearer t", "203.0.113.5", mine)).thenReturn(ResponseEntity.ok(body));
		when(feign.apiUsageList("Bearer t", "203.0.113.5", one)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.list("Bearer t", mine).getBody());
		assertEquals(body, controller.list("Bearer t", one).getBody());

		ApiUsageRequestDto bad = days(7);
		bad.setUserId("everyone");
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.list("Bearer t", bad)).getMessage());
	}
}
