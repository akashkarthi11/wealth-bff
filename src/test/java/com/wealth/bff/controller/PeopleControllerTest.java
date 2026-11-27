package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class PeopleControllerTest {

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	@Test
	void theListIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<PeopleOverviewDto, Void> body = new ApiResponse<>();
		when(feign.peopleList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).list("Bearer t").getBody());
	}

	@Test
	void theRefreshButtonIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<PeopleOverviewDto, Void> body = new ApiResponse<>();
		when(feign.peopleRefresh("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).refresh("Bearer t").getBody());
	}
}
