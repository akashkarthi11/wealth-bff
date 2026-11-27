package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ApiUsageDto;
import com.wealth.common.model.dto.admin.ApiUsageRequestDto;

// API usage (our own APIs and the outside APIs we call), counted and saved by the BSS. Admin only, decided in the BSS.
@RequestMapping(WealthConstants.API_USAGE_BASE)
public interface IfApiUsageContract {

	@PostMapping(WealthConstants.API_USAGE_LIST)
	ResponseEntity<ApiResponse<ApiUsageDto, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody(required = false) ApiUsageRequestDto request) throws KarthiException;
}
