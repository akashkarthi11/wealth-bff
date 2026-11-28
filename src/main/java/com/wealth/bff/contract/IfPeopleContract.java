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
import com.wealth.common.model.dto.people.BillSplitDto;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.model.dto.people.SplitCreateRequestDto;

// Signed-in users; the call needs the token and the BSS returns only that user's people.
@RequestMapping(WealthConstants.PEOPLE_BASE)
public interface IfPeopleContract {

	@PostMapping(WealthConstants.PEOPLE_LIST)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_REFRESH)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> refresh(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SPLIT_CANCEL)
	ResponseEntity<ApiResponse<Boolean, Void>> cancelSplit(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody IdRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SPLIT_CREATE)
	ResponseEntity<ApiResponse<BillSplitDto, Void>> createSplit(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody SplitCreateRequestDto request)
			throws KarthiException;
}
