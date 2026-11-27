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
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartyDto;
import com.wealth.common.model.dto.admin.ThirdPartySaveRequestDto;

// Third-party services (Settings > Connections). Admin only, decided in the BSS, which also keeps the keys encrypted.
@RequestMapping(WealthConstants.THIRD_PARTY_BASE)
public interface IfThirdPartyContract {

	@PostMapping(WealthConstants.THIRD_PARTY_LIST)
	ResponseEntity<ApiResponse<List<ThirdPartyDto>, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.THIRD_PARTY_SAVE)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> save(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ThirdPartySaveRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.THIRD_PARTY_TEST)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> test(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ProviderRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.THIRD_PARTY_FETCH)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> fetch(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ProviderRequestDto request) throws KarthiException;

	// Not admin only: the BSS decides what each caller sees.
	@PostMapping(WealthConstants.THIRD_PARTY_OVERVIEW)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.admin.ConnectionsOverviewDto, Void>> overview(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.THIRD_PARTY_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> remove(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ProviderRequestDto request) throws KarthiException;
}
