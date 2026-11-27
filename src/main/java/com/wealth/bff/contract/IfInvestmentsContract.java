package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.invest.HoldingDto;
import com.wealth.common.model.dto.invest.HoldingSaveRequestDto;
import com.wealth.common.model.dto.invest.InvestmentsDto;

// Signed-in users; the call needs the token and the BSS returns only that user's holdings.
@RequestMapping(WealthConstants.INVESTMENTS_BASE)
public interface IfInvestmentsContract {

	@PostMapping(WealthConstants.INVESTMENTS_REFRESH)
	ResponseEntity<ApiResponse<InvestmentsDto, Void>> refresh(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.INVESTMENTS_HOLDING_SAVE)
	ResponseEntity<ApiResponse<HoldingDto, Void>> saveHolding(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody HoldingSaveRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.INVESTMENTS_LIST)
	ResponseEntity<ApiResponse<InvestmentsDto, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
}
