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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class CustomerUserControllerTest {

	private WealthFeignClient feign;
	private CustomerUserController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new CustomerUserController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private LoginRequestDto login() {
		LoginRequestDto r = new LoginRequestDto();
		r.setEmail("owner@example.com");
		r.setPassword("a-long-enough-password");
		return r;
	}

	@Test
	void loginForwardsTheCallerIpAndPassesTheBssAnswerThrough() {
		ApiResponse<LoginResponseDto, Void> body = new ApiResponse<>();
		body.setStatus(1);
		when(feign.login(org.mockito.ArgumentMatchers.eq("203.0.113.5"), any(LoginRequestDto.class)))
				.thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED).body(body));
		ResponseEntity<ApiResponse<LoginResponseDto, Void>> out = controller.login(login());
		assertEquals(HttpStatus.ACCEPTED, out.getStatusCode());
		assertEquals(body, out.getBody());
	}

	@Test
	void anInvalidRequestNeverReachesTheBss() {
		LoginRequestDto bad = login();
		bad.setEmail("no-at-sign");
		KarthiException ex = assertThrows(KarthiException.class, () -> controller.login(bad));
		assertEquals(BaseError.INVALID_EMAIL_FORMAT, ex.getMessage());
		verify(feign, never()).login(any(), any());
	}

	@Test
	void theBearerTokenIsForwardedUnchangedOnProtectedCalls() {
		when(feign.me("Bearer abc", "203.0.113.5")).thenReturn(ResponseEntity.ok(new ApiResponse<>()));
		when(feign.logout("Bearer abc", "203.0.113.5")).thenReturn(ResponseEntity.ok(new ApiResponse<>()));
		controller.me("Bearer abc");
		verify(feign).me("Bearer abc", "203.0.113.5");
		controller.logout("Bearer abc");
		verify(feign).logout("Bearer abc", "203.0.113.5");
	}

	@Test
	void resetIsValidatedBeforeItIsForwarded() {
		PasswordResetRequestDto bad = new PasswordResetRequestDto();
		bad.setEmail("a@b.co");
		assertThrows(KarthiException.class, () -> controller.resetPassword("Bearer abc", bad));
		verify(feign, never()).resetPassword(any(), any(), any());
	}

	@Test
	void theProfileAndAPasswordChangeAreCheckedAtTheEdgeThenForwardedWithTheToken() {
		when(feign.profileSave(any(), any(), any())).thenReturn(org.springframework.http.ResponseEntity.ok(new com.wealth.common.generic.ApiResponse<>()));
		when(feign.changePassword(any(), any(), any())).thenReturn(org.springframework.http.ResponseEntity.ok(new com.wealth.common.generic.ApiResponse<>()));
		com.wealth.common.model.dto.customer.ProfileSaveRequestDto profile = new com.wealth.common.model.dto.customer.ProfileSaveRequestDto();
		profile.setDisplayName("Karthi");
		profile.setMobile("+91 98765 43210");
		controller.profileSave("Bearer t", profile);
		org.mockito.Mockito.verify(feign).profileSave("Bearer t", "203.0.113.5", profile);
		profile.setDisplayName("x".repeat(61));
		org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.profileSave("Bearer t", profile));
		profile.setDisplayName("a\nb");
		org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.profileSave("Bearer t", profile));
		org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.profileSave("Bearer t", null));

		com.wealth.common.model.dto.customer.PasswordChangeRequestDto change = new com.wealth.common.model.dto.customer.PasswordChangeRequestDto();
		change.setCurrentPassword("current-password-1");
		change.setNewPassword("another-long-password");
		controller.changePassword("Bearer t", change);
		org.mockito.Mockito.verify(feign).changePassword("Bearer t", "203.0.113.5", change);
		change.setNewPassword("");
		org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.changePassword("Bearer t", change));
		change.setNewPassword("p".repeat(201));
		org.junit.jupiter.api.Assertions.assertThrows(com.wealth.common.generic.KarthiException.class, () -> controller.changePassword("Bearer t", change));
	}
}
