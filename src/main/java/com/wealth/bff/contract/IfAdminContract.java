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
import com.wealth.common.model.dto.admin.RecordsRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
import com.wealth.common.model.dto.admin.RoleMenusRequestDto;
import com.wealth.common.model.dto.admin.RolesOverviewDto;
import com.wealth.common.model.dto.admin.SnapshotDto;

// Admin only, decided in the BSS. Every call needs the token.
@RequestMapping(WealthConstants.ADMIN_BASE)
public interface IfAdminContract {

	@PostMapping(WealthConstants.ROLES_LIST)
	ResponseEntity<ApiResponse<RolesOverviewDto, Void>> rolesList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.ROLES_UPDATE_MENUS)
	ResponseEntity<ApiResponse<RoleMenusDto, Void>> updateRoleMenus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody RoleMenusRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.RECORDS_LIST)
	ResponseEntity<ApiResponse<List<SnapshotDto>, Void>> recordsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody(required = false) RecordsRequestDto request) throws KarthiException;
}
