package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfIndstocksContract;
import com.wealth.bff.validator.IndstocksValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.IndstocksSaveRequestDto;
import com.wealth.common.model.dto.admin.IndstocksStatusDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class IndstocksController extends WealthBaseController implements IfIndstocksContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(IndstocksController.class);

	private final WealthFeignClient feignClient;

	public IndstocksController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> status(String authorization) throws KarthiException {
		LOGGER.info("Entering indstocks status BFF");
		return handleResponse(feignClient.indstocksStatus(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> save(String authorization, IndstocksSaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering indstocks save BFF");
		IndstocksValidator.validateSave(request);
		return handleResponse(feignClient.indstocksSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> test(String authorization) throws KarthiException {
		LOGGER.info("Entering indstocks test BFF");
		return handleResponse(feignClient.indstocksTest(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> remove(String authorization) throws KarthiException {
		LOGGER.info("Entering indstocks remove BFF");
		return handleResponse(feignClient.indstocksRemove(authorization, RequestHeaderUtil.getIp()));
	}
}
