package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.LendPaidRequestDto;
import com.wealth.common.model.dto.people.LendPaidResultDto;
import com.wealth.common.model.dto.people.LendRemarkRequestDto;
import com.wealth.common.model.dto.people.LendRemarkResultDto;
import com.wealth.common.model.dto.people.LendViewDto;
import com.wealth.common.model.dto.people.LendViewRequestDto;

// Public: no sign-in (the interceptor lets the /lend-view prefix through). The secret is in the body, never in an address.
@RequestMapping(WealthConstants.LEND_VIEW_BASE)
public interface IfLendViewContract {

	@PostMapping(WealthConstants.LEND_VIEW_VIEW)
	ResponseEntity<ApiResponse<LendViewDto, Void>> view(@RequestBody LendViewRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.LEND_VIEW_PAID)
	ResponseEntity<ApiResponse<LendPaidResultDto, Void>> paid(@RequestBody LendPaidRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.LEND_VIEW_REMARK)
	ResponseEntity<ApiResponse<LendRemarkResultDto, Void>> remark(@RequestBody LendRemarkRequestDto request) throws KarthiException;
}
