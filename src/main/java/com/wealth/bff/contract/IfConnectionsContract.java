package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

// Admin only: the BFF checks the caller's role with the BSS. The address changes live and returns to the deployed one on restart.
@RequestMapping(WealthConstants.CONNECTIONS_BASE)
public interface IfConnectionsContract {

	@PostMapping(WealthConstants.CONNECTION_BSS_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> bssGet(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.CONNECTION_BSS_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> bssUpdate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody ConnectionRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.CONNECTION_BSS_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> bssReset(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
}
