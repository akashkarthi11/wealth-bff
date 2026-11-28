package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfSmsContract;
import com.wealth.bff.validator.SmsValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.sms.SmsDeviceCreateRequestDto;
import com.wealth.common.model.dto.sms.SmsDeviceDto;
import com.wealth.common.model.dto.sms.SmsPushRequestDto;
import com.wealth.common.model.dto.sms.SmsPushResultDto;
import com.wealth.common.model.dto.sms.SmsTokenRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class SmsController extends WealthBaseController implements IfSmsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(SmsController.class);

	private final WealthFeignClient feignClient;

	public SmsController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<SmsDeviceDto, Void>> createDevice(String authorization, SmsDeviceCreateRequestDto request) throws KarthiException {
		LOGGER.info("Entering sms device create BFF");
		SmsValidator.validateCreate(request);
		return handleResponse(feignClient.smsDeviceCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<SmsDeviceDto>, Void>> listDevices(String authorization) throws KarthiException {
		LOGGER.info("Entering sms device list BFF");
		return handleResponse(feignClient.smsDeviceList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> revokeDevice(String authorization, IdRequestDto request) throws KarthiException {
		LOGGER.info("Entering sms device revoke BFF");
		SmsValidator.validateId(request);
		return handleResponse(feignClient.smsDeviceRevoke(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<SmsPushResultDto, Void>> push(SmsPushRequestDto request) throws KarthiException {
		LOGGER.info("Entering sms push BFF");
		SmsValidator.validatePush(request);
		return handleResponse(feignClient.smsPush(RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<String>, Void>> banks(SmsTokenRequestDto request) throws KarthiException {
		LOGGER.info("Entering sms banks BFF");
		SmsValidator.validateToken(request == null ? null : request.getToken());
		return handleResponse(feignClient.smsBanks(RequestHeaderUtil.getIp(), request));
	}
}
