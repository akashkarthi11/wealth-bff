package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.IndstocksSaveRequestDto;
import com.wealth.common.model.dto.admin.IndstocksStatusDto;

// The signed-in user's own INDstocks connection (token, test, remove). Every call is for the caller's own token.
@RequestMapping(WealthConstants.INDSTOCKS_BASE)
public interface IfIndstocksContract {

	@PostMapping(WealthConstants.BROKER_STATUS)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> status(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_SAVE)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> save(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody IndstocksSaveRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_TEST)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> test(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> remove(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
}
