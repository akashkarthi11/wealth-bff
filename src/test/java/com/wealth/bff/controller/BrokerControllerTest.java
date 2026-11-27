package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.BrokerUserSaveRequestDto;
import com.wealth.common.model.dto.admin.BrokerUserStatusDto;

class BrokerControllerTest {

	private WealthFeignClient feign;
	private BrokerController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new BrokerController(feign);
	}

	@Test
	void everyCallIsForwardedWithTheTokenAndALoginIsCheckedAtTheEdge() {
		ApiResponse<BrokerUserStatusDto, Void> status = new ApiResponse<>();
		when(feign.brokerStatus(any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.brokerSave(any(), any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.brokerTest(any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.brokerFetch(any(), any())).thenReturn(ResponseEntity.ok(status));
		when(feign.brokerRemove(any(), any())).thenReturn(ResponseEntity.ok(new ApiResponse<>()));

		assertEquals(status, controller.status("Bearer t").getBody());
		assertEquals(status, controller.test("Bearer t").getBody());
		assertEquals(status, controller.fetch("Bearer t").getBody());
		controller.remove("Bearer t");

		BrokerUserSaveRequestDto login = new BrokerUserSaveRequestDto();
		login.setApiKey("key");
		login.setPin("1234");
		assertEquals(status, controller.save("Bearer t", login).getBody());

		login.setApiKey("x".repeat(201));
		assertThrows(KarthiException.class, () -> controller.save("Bearer t", login));
		login.setApiKey("a\nb");
		assertThrows(KarthiException.class, () -> controller.save("Bearer t", login));
		assertThrows(KarthiException.class, () -> controller.save("Bearer t", null));
		verify(feign, org.mockito.Mockito.times(1)).brokerSave(any(), any(), any());
		verify(feign, never()).brokerSave(any(), any(), org.mockito.ArgumentMatchers.isNull());
	}
}
