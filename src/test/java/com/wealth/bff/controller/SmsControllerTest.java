package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.sms.SmsDeviceCreateRequestDto;
import com.wealth.common.model.dto.sms.SmsMessageDto;
import com.wealth.common.model.dto.sms.SmsPushRequestDto;
import com.wealth.common.model.dto.sms.SmsPushResultDto;
import com.wealth.common.model.dto.sms.SmsTokenRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class SmsControllerTest {

	private static final String TOKEN = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private static SmsMessageDto message(String body) {
		SmsMessageDto message = new SmsMessageDto();
		message.setSender("VM-HDFCBK-S");
		message.setBody(body);
		message.setAt(1L);
		return message;
	}

	private static SmsPushRequestDto push(String token, List<SmsMessageDto> messages) {
		SmsPushRequestDto request = new SmsPushRequestDto();
		request.setToken(token);
		request.setMessages(messages);
		return request;
	}

	@Test
	void aPushIsForwardedWithOnlyTheCallersAddress() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		SmsPushRequestDto request = push(TOKEN, new ArrayList<>(List.of(message("Rs.5 debited"))));
		ApiResponse<SmsPushResultDto, Void> body = new ApiResponse<>();
		when(feign.smsPush("203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new SmsController(feign).push(request).getBody());
	}

	@Test
	void theBankListIsForwardedWithTheCallersAddressAndNeedsATokenShape() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		SmsTokenRequestDto request = new SmsTokenRequestDto();
		request.setToken(TOKEN);
		ApiResponse<List<String>, Void> body = new ApiResponse<>();
		when(feign.smsBanks("203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		SmsController controller = new SmsController(feign);

		assertEquals(body, controller.banks(request).getBody());
		request.setToken("bad token!");
		assertEquals(BaseError.SMS_TOKEN_INVALID, assertThrows(KarthiException.class, () -> controller.banks(request)).getMessage());
		assertEquals(BaseError.SMS_TOKEN_INVALID, assertThrows(KarthiException.class, () -> controller.banks(null)).getMessage());
		verify(feign).smsBanks(any(), any(SmsTokenRequestDto.class));
	}

	@Test
	void aBadTokenOrBodyIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		SmsController controller = new SmsController(feign);
		List<SmsMessageDto> one = new ArrayList<>(List.of(message("Rs.5 debited")));

		for (String bad : new String[] { null, "", "has space", "x".repeat(65), "../../etc" }) {
			assertEquals(BaseError.SMS_TOKEN_INVALID, assertThrows(KarthiException.class, () -> controller.push(push(bad, one))).getMessage(), String.valueOf(bad));
		}
		assertThrows(KarthiException.class, () -> controller.push(null));
		assertEquals(BaseError.SMS_PUSH_INVALID, assertThrows(KarthiException.class, () -> controller.push(push(TOKEN, null))).getMessage());
		assertEquals(BaseError.SMS_PUSH_INVALID, assertThrows(KarthiException.class, () -> controller.push(push(TOKEN, new ArrayList<>()))).getMessage());
		List<SmsMessageDto> many = new ArrayList<>();
		for (int i = 0; i < 51; i++) {
			many.add(message("Rs.5 debited"));
		}
		assertEquals(BaseError.SMS_PUSH_INVALID, assertThrows(KarthiException.class, () -> controller.push(push(TOKEN, many))).getMessage());
		assertEquals(BaseError.SMS_PUSH_INVALID, assertThrows(KarthiException.class, () -> controller.push(push(TOKEN, new ArrayList<>(List.of(message("x".repeat(1001))))))).getMessage());
		verify(feign, never()).smsPush(any(), any());
	}

	@Test
	void aPhoneNameIsCheckedAtTheEdge() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		SmsController controller = new SmsController(feign);
		SmsDeviceCreateRequestDto request = new SmsDeviceCreateRequestDto();
		request.setLabel("x".repeat(81));
		assertEquals(BaseError.SMS_LABEL_INVALID, assertThrows(KarthiException.class, () -> controller.createDevice("Bearer t", request)).getMessage());
		assertThrows(KarthiException.class, () -> controller.createDevice("Bearer t", null));
		verify(feign, never()).smsDeviceCreate(any(), any(), any());
	}
}
