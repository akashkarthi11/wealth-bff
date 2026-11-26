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

// Service to service (the UI picking its BFF address when it boots): no user token, the shared service key is passed to the BSS, which checks it.
@RequestMapping(WealthConstants.INTERNAL_SETTINGS_BASE)
public interface IfInternalSettingsContract {

	@PostMapping(WealthConstants.INTERNAL_PICK)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> pick(@RequestHeader(value = WealthConstants.HEADER_SERVICE_KEY, required = false) String serviceKey,
			@RequestBody ConnectionRequestDto request) throws KarthiException;
}
