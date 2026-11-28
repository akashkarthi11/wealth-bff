package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfLendViewContract;
import com.wealth.bff.validator.PeopleValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.people.LendPaidRequestDto;
import com.wealth.common.model.dto.people.LendPaidResultDto;
import com.wealth.common.model.dto.people.LendRemarkRequestDto;
import com.wealth.common.model.dto.people.LendRemarkResultDto;
import com.wealth.common.model.dto.people.LendViewDto;
import com.wealth.common.model.dto.people.LendViewRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class LendViewController extends WealthBaseController implements IfLendViewContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(LendViewController.class);

	private final WealthFeignClient feignClient;

	public LendViewController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<LendViewDto, Void>> view(LendViewRequestDto request) throws KarthiException {
		LOGGER.info("Entering lend view BFF");
		PeopleValidator.validateLendView(request);
		return handleResponse(feignClient.lendView(RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<LendPaidResultDto, Void>> paid(LendPaidRequestDto request) throws KarthiException {
		LOGGER.info("Entering lend paid BFF");
		PeopleValidator.validateLendPaid(request);
		return handleResponse(feignClient.lendPaid(RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<LendRemarkResultDto, Void>> remark(LendRemarkRequestDto request) throws KarthiException {
		LOGGER.info("Entering lend remark BFF");
		PeopleValidator.validateLendRemark(request);
		return handleResponse(feignClient.lendRemark(RequestHeaderUtil.getIp(), request));
	}
}
