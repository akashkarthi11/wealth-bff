package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfAccountsContract;
import com.wealth.bff.validator.AccountsValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.account.AccountDetailDto;
import com.wealth.common.model.dto.account.AccountSplitDto;
import com.wealth.common.model.dto.account.AccountSubtypeDto;
import com.wealth.common.model.dto.account.AccountsOverviewDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class AccountsController extends WealthBaseController implements IfAccountsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(AccountsController.class);

	private final WealthFeignClient feignClient;

	public AccountsController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.accountsList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<AccountDetailDto, Void>> detail(String authorization, IdRequestDto request) throws KarthiException {
		LOGGER.info("Entering detail BFF");
		AccountsValidator.validateId(request);
		return handleResponse(feignClient.accountsDetail(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<AccountSubtypeDto>, Void>> subtypes(String authorization) throws KarthiException {
		LOGGER.info("Entering subtypes BFF");
		return handleResponse(feignClient.accountsSubtypes(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<List<AccountSplitDto>, Void>> splitsList(String authorization, AccountIdRequestDto request)
			throws KarthiException {
		LOGGER.info("Entering splitsList BFF");
		AccountsValidator.validateAccountId(request);
		return handleResponse(feignClient.accountsSplitsList(authorization, RequestHeaderUtil.getIp(), request));
	}
}
