package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.LendPaidRequestDto;
import com.wealth.common.model.dto.people.LendPaidResultDto;
import com.wealth.common.model.dto.people.LendRemarkRequestDto;
import com.wealth.common.model.dto.people.LendRemarkResultDto;
import com.wealth.common.model.dto.people.LendViewDto;
import com.wealth.common.model.dto.people.LendViewRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class LendViewControllerTest {

	private static final String TOKEN = "abcdefghijklmnopqrstuvwxyz012345";

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private static LendViewRequestDto view(String token) {
		LendViewRequestDto request = new LendViewRequestDto();
		request.setToken(token);
		return request;
	}

	@Test
	void theSecretIsForwardedWithOnlyTheCallersAddress() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		LendViewRequestDto request = view(TOKEN);
		ApiResponse<LendViewDto, Void> body = new ApiResponse<>();
		when(feign.lendView("203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new LendViewController(feign).view(request).getBody());
	}

	@Test
	void aSecretThatCannotBeRealIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		LendViewController controller = new LendViewController(feign);

		for (String bad : new String[] { null, "", "short", "x".repeat(65), "has space and more characters!!!!!!!!!!!", "../../etc/passwd/../../../////////" }) {
			assertThrows(KarthiException.class, () -> controller.view(view(bad)), String.valueOf(bad));
		}
		assertThrows(KarthiException.class, () -> controller.view(null));
		verify(feign, never()).lendView(any(), any());
	}

	@Test
	void aPaymentMessageAndARemarkAreForwardedWithOnlyTheCallersAddress() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		LendViewController controller = new LendViewController(feign);
		LendPaidRequestDto paid = new LendPaidRequestDto();
		paid.setToken(TOKEN);
		ApiResponse<LendPaidResultDto, Void> paidBody = new ApiResponse<>();
		when(feign.lendPaid("203.0.113.5", paid)).thenReturn(ResponseEntity.ok(paidBody));
		assertEquals(paidBody, controller.paid(paid).getBody());
		LendRemarkRequestDto remark = new LendRemarkRequestDto();
		remark.setToken(TOKEN);
		ApiResponse<LendRemarkResultDto, Void> remarkBody = new ApiResponse<>();
		when(feign.lendRemark("203.0.113.5", remark)).thenReturn(ResponseEntity.ok(remarkBody));
		assertEquals(remarkBody, controller.remark(remark).getBody());
	}

	@Test
	void aMessageOrRemarkWithASecretThatCannotBeRealOrTooMuchTextIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		LendViewController controller = new LendViewController(feign);
		LendPaidRequestDto badPaid = new LendPaidRequestDto();
		badPaid.setToken("short");
		assertThrows(KarthiException.class, () -> controller.paid(badPaid));
		LendPaidRequestDto longNote = new LendPaidRequestDto();
		longNote.setToken(TOKEN);
		longNote.setNote("x".repeat(700));
		assertThrows(KarthiException.class, () -> controller.paid(longNote));
		LendRemarkRequestDto badRemark = new LendRemarkRequestDto();
		badRemark.setToken("../../x");
		assertThrows(KarthiException.class, () -> controller.remark(badRemark));
		LendRemarkRequestDto longBody = new LendRemarkRequestDto();
		longBody.setToken(TOKEN);
		longBody.setBody("x".repeat(700));
		assertThrows(KarthiException.class, () -> controller.remark(longBody));
		assertThrows(KarthiException.class, () -> controller.paid(null));
		assertThrows(KarthiException.class, () -> controller.remark(null));
		verify(feign, never()).lendPaid(any(), any());
		verify(feign, never()).lendRemark(any(), any());
	}
}
