package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfBrokerContract;
import com.wealth.bff.validator.BrokerValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.BrokerUserSaveRequestDto;
import com.wealth.common.model.dto.admin.BrokerUserStatusDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class BrokerController extends WealthBaseController implements IfBrokerContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(BrokerController.class);

	private final WealthFeignClient feignClient;

	public BrokerController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> status(String authorization) throws KarthiException {
		LOGGER.info("Entering broker status BFF");
		return handleResponse(feignClient.brokerStatus(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> save(String authorization, BrokerUserSaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering broker save BFF");
		BrokerValidator.validateSave(request);
		return handleResponse(feignClient.brokerSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> test(String authorization) throws KarthiException {
		LOGGER.info("Entering broker test BFF");
		return handleResponse(feignClient.brokerTest(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> fetch(String authorization) throws KarthiException {
		LOGGER.info("Entering broker fetch BFF");
		return handleResponse(feignClient.brokerFetch(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> remove(String authorization) throws KarthiException {
		LOGGER.info("Entering broker remove BFF");
		return handleResponse(feignClient.brokerRemove(authorization, RequestHeaderUtil.getIp()));
	}
}
