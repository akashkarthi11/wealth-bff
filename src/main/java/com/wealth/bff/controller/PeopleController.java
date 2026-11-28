package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfPeopleContract;
import com.wealth.bff.validator.AccountsValidator;
import com.wealth.bff.validator.PeopleValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.people.BillSplitDto;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.model.dto.people.SplitCreateRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class PeopleController extends WealthBaseController implements IfPeopleContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(PeopleController.class);

	private final WealthFeignClient feignClient;

	public PeopleController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.peopleList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> refresh(String authorization) throws KarthiException {
		LOGGER.info("Entering refresh BFF");
		return handleResponse(feignClient.peopleRefresh(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<BillSplitDto, Void>> createSplit(String authorization, SplitCreateRequestDto request) throws KarthiException {
		LOGGER.info("Entering createSplit BFF");
		PeopleValidator.validateSplit(request);
		return handleResponse(feignClient.peopleSplitCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> cancelSplit(String authorization, IdRequestDto request) throws KarthiException {
		LOGGER.info("Entering cancelSplit BFF");
		AccountsValidator.validateId(request);
		return handleResponse(feignClient.peopleSplitCancel(authorization, RequestHeaderUtil.getIp(), request));
	}
}
