package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.BrokerUserSaveRequestDto;
import com.wealth.common.model.dto.admin.BrokerUserStatusDto;

// The signed-in user's own Angel One connection (login, test, fetch now, remove). Every call is for the caller's own login.
@RequestMapping(WealthConstants.BROKER_BASE)
public interface IfBrokerContract {

	@PostMapping(WealthConstants.BROKER_STATUS)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> status(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_SAVE)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> save(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody BrokerUserSaveRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_TEST)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> test(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_FETCH)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> fetch(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.BROKER_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> remove(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
}
