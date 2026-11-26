package com.wealth.bff.contract;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.account.AccountDetailDto;
import com.wealth.common.model.dto.account.AccountSplitDto;
import com.wealth.common.model.dto.account.AccountSubtypeDto;
import com.wealth.common.model.dto.account.AccountsOverviewDto;

// Signed-in users; each call needs the token and the BSS returns only that user's rows.
@RequestMapping(WealthConstants.ACCOUNTS_BASE)
public interface IfAccountsContract {

	@PostMapping(WealthConstants.ACCOUNTS_LIST)
	ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> list(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.ACCOUNTS_DETAIL)
	ResponseEntity<ApiResponse<AccountDetailDto, Void>> detail(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody IdRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.ACCOUNTS_SUBTYPES)
	ResponseEntity<ApiResponse<List<AccountSubtypeDto>, Void>> subtypes(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.ACCOUNTS_SPLITS_LIST)
	ResponseEntity<ApiResponse<List<AccountSplitDto>, Void>> splitsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody AccountIdRequestDto request)
			throws KarthiException;
}
