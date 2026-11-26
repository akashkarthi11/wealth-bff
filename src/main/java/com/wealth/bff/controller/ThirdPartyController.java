package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfThirdPartyContract;
import com.wealth.bff.validator.ThirdPartyValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartyDto;
import com.wealth.common.model.dto.admin.ThirdPartySaveRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class ThirdPartyController extends WealthBaseController implements IfThirdPartyContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(ThirdPartyController.class);

	private final WealthFeignClient feignClient;

	public ThirdPartyController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<List<ThirdPartyDto>, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.thirdPartyList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ThirdPartyDto, Void>> save(String authorization, ThirdPartySaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering save BFF");
		ThirdPartyValidator.validateSave(request);
		return handleResponse(feignClient.thirdPartySave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ThirdPartyDto, Void>> test(String authorization, ProviderRequestDto request) throws KarthiException {
		LOGGER.info("Entering test BFF");
		ThirdPartyValidator.validateProvider(request);
		return handleResponse(feignClient.thirdPartyTest(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ThirdPartyDto, Void>> fetch(String authorization, ProviderRequestDto request) throws KarthiException {
		LOGGER.info("Entering fetch BFF");
		ThirdPartyValidator.validateProvider(request);
		return handleResponse(feignClient.thirdPartyFetch(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> remove(String authorization, ProviderRequestDto request) throws KarthiException {
		LOGGER.info("Entering remove BFF");
		ThirdPartyValidator.validateProvider(request);
		return handleResponse(feignClient.thirdPartyRemove(authorization, RequestHeaderUtil.getIp(), request));
	}
}
