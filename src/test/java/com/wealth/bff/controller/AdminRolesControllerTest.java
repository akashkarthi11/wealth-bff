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
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.RoleCreateRequestDto;
import com.wealth.common.model.dto.admin.RoleDto;
import com.wealth.common.model.dto.admin.UserAccessDto;
import com.wealth.common.model.dto.admin.UserRoleRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class AdminRolesControllerTest {

	private static final String USER_ID = "3f2b8c1e-0a4d-4c55-9d0e-1b6c7a8d9e11";

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

	private RoleCreateRequestDto named(String label) {
		RoleCreateRequestDto r = new RoleCreateRequestDto();
		r.setLabel(label);
		return r;
	}

	private UserRoleRequestDto change(String userId, String role) {
		UserRoleRequestDto r = new UserRoleRequestDto();
		r.setUserId(userId);
		r.setRole(role);
		return r;
	}

	@Test
	void addingARoleIsCheckedAtTheEdgeThenForwarded() {
		RoleCreateRequestDto ok = named("Family");
		ApiResponse<RoleDto, Void> body = new ApiResponse<>();
		when(feign.adminCreateRole("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.createRole("Bearer t", ok).getBody());
		for (String bad : new String[] { null, "", "  ", "x".repeat(61) }) {
			assertEquals(BaseError.ROLE_NAME_INVALID, assertThrows(KarthiException.class, () -> controller.createRole("Bearer t", named(bad))).getMessage());
		}
		assertThrows(KarthiException.class, () -> controller.createRole("Bearer t", null));
	}

	@Test
	void theUsersListIsForwardedWithTheToken() {
		ApiResponse<List<UserAccessDto>, Void> body = new ApiResponse<>();
		when(feign.adminUsersList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.usersList("Bearer t").getBody());
	}

	@Test
	void aRoleChangeNeedsARealUserIdAndARoleKeyBeforeItIsForwarded() {
		UserRoleRequestDto ok = change(USER_ID, "family");
		ApiResponse<UserAccessDto, Void> body = new ApiResponse<>();
		when(feign.adminSetUserRole("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.setUserRole("Bearer t", ok).getBody());

		for (UserRoleRequestDto bad : new UserRoleRequestDto[] { change("1; drop table", "user"), change(null, "user"), change(USER_ID, null), change(USER_ID, "Bad Role"),
				change(USER_ID, "x".repeat(31)), null }) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.setUserRole("Bearer t", bad)).getMessage());
		}
		verify(feign, never()).adminSetUserRole(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && "Bad Role".equals(r.getRole())));
	}
}
