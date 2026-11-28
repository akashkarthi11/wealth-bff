package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.BillSplitDto;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.model.dto.people.SplitCreateRequestDto;
import com.wealth.common.model.dto.people.SplitPersonRequestDto;
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

	private static SplitCreateRequestDto split() {
		SplitCreateRequestDto request = new SplitCreateRequestDto();
		request.setTransactionId("11111111-1111-4111-8111-111111111111");
		request.setMode("equal");
		SplitPersonRequestDto person = new SplitPersonRequestDto();
		person.setName("Arun S");
		request.getPeople().add(person);
		return request;
	}

	@Test
	void aNewSplitIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		SplitCreateRequestDto request = split();
		ApiResponse<BillSplitDto, Void> body = new ApiResponse<>();
		when(feign.peopleSplitCreate("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).createSplit("Bearer t", request).getBody());
	}

	@Test
	void cancellingASplitIsForwardedAndABadIdIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		com.wealth.common.model.dto.IdRequestDto request = new com.wealth.common.model.dto.IdRequestDto();
		request.setId("11111111-1111-4111-8111-111111111111");
		ApiResponse<Boolean, Void> body = new ApiResponse<>();
		when(feign.peopleSplitCancel("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.cancelSplit("Bearer t", request).getBody());
		assertThrows(KarthiException.class, () -> controller.cancelSplit("Bearer t", new com.wealth.common.model.dto.IdRequestDto()));
	}

	@Test
	void aSplitWithoutATransactionOrPeopleOrWithTooManyIsRefusedBeforeTheBss() {
		PeopleController controller = new PeopleController(mock(WealthFeignClient.class));
		SplitCreateRequestDto none = split();
		none.getPeople().clear();
		SplitCreateRequestDto noTransaction = split();
		noTransaction.setTransactionId(null);
		SplitCreateRequestDto many = split();
		for (int i = 0; i < 50; i++) {
			many.getPeople().add(new SplitPersonRequestDto());
		}
		SplitCreateRequestDto longName = split();
		longName.getPeople().get(0).setName("x".repeat(161));

		for (SplitCreateRequestDto bad : new SplitCreateRequestDto[] { none, noTransaction, many, longName, null }) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.createSplit("Bearer t", bad)).getMessage());
		}
	}
}
