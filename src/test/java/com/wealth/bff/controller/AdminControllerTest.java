package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.RecordsRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
import com.wealth.common.model.dto.admin.RoleMenusRequestDto;
import com.wealth.common.model.dto.admin.RolesOverviewDto;
import com.wealth.common.model.dto.admin.SnapshotDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class AdminControllerTest {

	private WealthFeignClient feign;
	private AdminController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new AdminController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private RoleMenusRequestDto request(String role, String... menus) {
		RoleMenusRequestDto r = new RoleMenusRequestDto();
		r.setRole(role);
		r.setMenus(List.of(menus));
		return r;
	}

	@Test
	void rolesListForwardsTheTokenAndTheCallerIpAndReturnsTheBssAnswerAsIs() {
		ApiResponse<RolesOverviewDto, Void> body = new ApiResponse<>();
		when(feign.adminRolesList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(body));

		ResponseEntity<ApiResponse<RolesOverviewDto, Void>> out = controller.rolesList("Bearer t");

		assertEquals(HttpStatus.ACCEPTED, out.getStatusCode());
		assertEquals(body, out.getBody());
	}

	@Test
	void updateMenusIsCheckedAtTheEdgeThenForwarded() {
		RoleMenusRequestDto ok = request("user", "overview", "banks");
		when(feign.adminUpdateRoleMenus("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(new ApiResponse<RoleMenusDto, Void>()));
		assertEquals(HttpStatus.OK, controller.updateRoleMenus("Bearer t", ok).getStatusCode());
		verify(feign).adminUpdateRoleMenus("Bearer t", "203.0.113.5", ok);

		KarthiException bad = assertThrows(KarthiException.class, () -> controller.updateRoleMenus("Bearer t", request("user", "Not A Key")));
		assertEquals(BaseError.INVALID_REQUEST, bad.getMessage());
		verify(feign, never()).adminUpdateRoleMenus(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && r.getMenus().contains("Not A Key")));
	}

	@Test
	void recordsSearchIsLimitedAndTheBodyMayBeEmpty() {
		when(feign.adminRecordsList(any(), any(), any())).thenReturn(ResponseEntity.ok(new ApiResponse<List<SnapshotDto>, Void>()));
		assertEquals(HttpStatus.OK, controller.recordsList("Bearer t", null).getStatusCode());

		RecordsRequestDto longSearch = new RecordsRequestDto();
		longSearch.setQ("x".repeat(WealthConstants.SEARCH_MAX_LENGTH + 1));
		assertThrows(KarthiException.class, () -> controller.recordsList("Bearer t", longSearch));
	}
}
