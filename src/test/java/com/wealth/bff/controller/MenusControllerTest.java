package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.menu.NavMenuDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class MenusControllerTest {

	private WealthFeignClient feign;
	private MenusController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new MenusController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	@Test
	void theNavigationIsAskedFromTheBssWithTheCallersTokenAndPassedOnAsIs() {
		ApiResponse<List<NavMenuDto>, Void> body = new ApiResponse<>();
		when(feign.menusList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(body));

		ResponseEntity<ApiResponse<List<NavMenuDto>, Void>> out = controller.list("Bearer t");

		assertEquals(HttpStatus.ACCEPTED, out.getStatusCode());
		assertEquals(body, out.getBody());
	}
}
