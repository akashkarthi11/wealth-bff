package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfTransactionsContract;
import com.wealth.bff.validator.AccountsValidator;
import com.wealth.bff.validator.TransactionsValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.transaction.TransactionCreateRequestDto;
import com.wealth.common.model.dto.transaction.TransactionDto;
import com.wealth.common.model.dto.transaction.TransactionPageDto;
import com.wealth.common.model.dto.transaction.TransactionPageRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowItemDto;
import java.util.List;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class TransactionsController extends WealthBaseController implements IfTransactionsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(TransactionsController.class);

	private final WealthFeignClient feignClient;

	public TransactionsController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<TransactionPageDto, Void>> list(String authorization, TransactionPageRequestDto request) throws KarthiException {
		LOGGER.info("Entering list BFF");
		TransactionsValidator.validatePage(request);
		return handleResponse(feignClient.transactionsList(authorization, RequestHeaderUtil.getIp(), request == null ? new TransactionPageRequestDto() : request));
	}

	@Override
	public ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.TransactionSummaryDto, Void>> summary(String authorization, TransactionPageRequestDto request) throws KarthiException {
		LOGGER.info("Entering summary BFF");
		TransactionsValidator.validatePage(request);
		return handleResponse(feignClient.transactionsSummary(authorization, RequestHeaderUtil.getIp(), request == null ? new TransactionPageRequestDto() : request));
	}

	@Override
	public ResponseEntity<ApiResponse<TransactionPageDto, Void>> refresh(String authorization) throws KarthiException {
		LOGGER.info("Entering refresh BFF");
		return handleResponse(feignClient.transactionsRefresh(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<TransactionDto, Void>> create(String authorization, TransactionCreateRequestDto request) throws KarthiException {
		LOGGER.info("Entering create BFF");
		TransactionsValidator.validateCreate(request);
		return handleResponse(feignClient.transactionsCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<WorkflowItemDto>, Void>> workflowList(String authorization) throws KarthiException {
		LOGGER.info("Entering workflowList BFF");
		return handleResponse(feignClient.transactionsWorkflowList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> workflowDecide(String authorization, WorkflowDecisionRequestDto request) throws KarthiException {
		LOGGER.info("Entering workflowDecide BFF");
		TransactionsValidator.validateDecision(request);
		return handleResponse(feignClient.transactionsWorkflowDecide(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> delete(String authorization, IdRequestDto request) throws KarthiException {
		LOGGER.info("Entering delete BFF");
		AccountsValidator.validateId(request);
		return handleResponse(feignClient.transactionsDelete(authorization, RequestHeaderUtil.getIp(), request));
	}
}
