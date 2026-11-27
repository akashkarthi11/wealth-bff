package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfInvestmentsContract;
import com.wealth.bff.validator.InvestmentsValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.invest.HoldingDto;
import com.wealth.common.model.dto.invest.HoldingSaveRequestDto;
import com.wealth.common.model.dto.invest.InvestmentsDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class InvestmentsController extends WealthBaseController implements IfInvestmentsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(InvestmentsController.class);

	private final WealthFeignClient feignClient;

	public InvestmentsController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<InvestmentsDto, Void>> refresh(String authorization) throws KarthiException {
		LOGGER.info("Entering refresh BFF");
		return handleResponse(feignClient.investmentsRefresh(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<HoldingDto, Void>> saveHolding(String authorization, HoldingSaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering saveHolding BFF");
		InvestmentsValidator.validateHolding(request);
		return handleResponse(feignClient.investmentsSaveHolding(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<InvestmentsDto, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.investmentsList(authorization, RequestHeaderUtil.getIp()));
	}
}
