package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfInternalSettingsContract;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

@RestController
public class InternalSettingsController extends WealthBaseController implements IfInternalSettingsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(InternalSettingsController.class);

	private final WealthFeignClient feignClient;

	public InternalSettingsController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> pick(String serviceKey, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering pick BFF");
		return handleResponse(feignClient.internalSettingsPick(serviceKey, request));
	}
}
