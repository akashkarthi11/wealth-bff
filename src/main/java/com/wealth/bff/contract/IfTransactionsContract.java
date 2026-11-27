package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.transaction.TransactionCreateRequestDto;
import com.wealth.common.model.dto.transaction.TransactionDto;
import com.wealth.common.model.dto.transaction.TransactionPageDto;
import com.wealth.common.model.dto.transaction.TransactionPageRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowItemDto;
import java.util.List;

// Signed-in users; each call needs the token and the BSS touches only that user's transactions.
@RequestMapping(WealthConstants.TRANSACTIONS_BASE)
public interface IfTransactionsContract {

	@PostMapping(WealthConstants.TRANSACTIONS_LIST)
	ResponseEntity<ApiResponse<TransactionPageDto, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody(required = false) TransactionPageRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_SUMMARY)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.TransactionSummaryDto, Void>> summary(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody(required = false) TransactionPageRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_REFRESH)
	ResponseEntity<ApiResponse<TransactionPageDto, Void>> refresh(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_CREATE)
	ResponseEntity<ApiResponse<TransactionDto, Void>> create(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody TransactionCreateRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_WORKFLOW_LIST)
	ResponseEntity<ApiResponse<List<WorkflowItemDto>, Void>> workflowList(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_WORKFLOW_DECIDE)
	ResponseEntity<ApiResponse<Boolean, Void>> workflowDecide(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody WorkflowDecisionRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.TRANSACTIONS_DELETE)
	ResponseEntity<ApiResponse<Boolean, Void>> delete(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody IdRequestDto request)
			throws KarthiException;
}
