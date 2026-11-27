package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfApiUsageContract;
import com.wealth.bff.validator.ApiUsageValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ApiUsageDto;
import com.wealth.common.model.dto.admin.ApiUsageRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class ApiUsageController extends WealthBaseController implements IfApiUsageContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(ApiUsageController.class);

	private final WealthFeignClient feignClient;

	public ApiUsageController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<ApiUsageDto, Void>> list(String authorization, ApiUsageRequestDto request) throws KarthiException {
		LOGGER.info("Entering list api usage BFF");
		ApiUsageValidator.validate(request);
		return handleResponse(feignClient.apiUsageList(authorization, RequestHeaderUtil.getIp(), request));
	}
}
