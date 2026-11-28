package com.wealth.bff.contract;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.sms.SmsDeviceCreateRequestDto;
import com.wealth.common.model.dto.sms.SmsDeviceDto;
import com.wealth.common.model.dto.sms.SmsPushRequestDto;
import com.wealth.common.model.dto.sms.SmsPushResultDto;
import com.wealth.common.model.dto.sms.SmsTokenRequestDto;

// The device calls need a sign-in. The push is public (the interceptor lets the /sms-ingest prefix through): the phone's token is in the body, never in an address.
public interface IfSmsContract {

	@PostMapping(WealthConstants.SMS_DEVICE_CREATE)
	ResponseEntity<ApiResponse<SmsDeviceDto, Void>> createDevice(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody SmsDeviceCreateRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.SMS_DEVICE_LIST)
	ResponseEntity<ApiResponse<List<SmsDeviceDto>, Void>> listDevices(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.SMS_DEVICE_REVOKE)
	ResponseEntity<ApiResponse<Boolean, Void>> revokeDevice(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody IdRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.SMS_PUSH)
	ResponseEntity<ApiResponse<SmsPushResultDto, Void>> push(@RequestBody SmsPushRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.SMS_BANKS)
	ResponseEntity<ApiResponse<List<String>, Void>> banks(@RequestBody SmsTokenRequestDto request) throws KarthiException;
}
