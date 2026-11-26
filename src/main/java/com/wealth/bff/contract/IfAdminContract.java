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
import com.wealth.common.model.dto.admin.RoleCreateRequestDto;
import com.wealth.common.model.dto.admin.RoleDto;
import com.wealth.common.model.dto.admin.UserAccessDto;
import com.wealth.common.model.dto.admin.UserRoleRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
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
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody RoleMenusDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.ROLES_CREATE)
	ResponseEntity<ApiResponse<RoleDto, Void>> createRole(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody RoleCreateRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.USERS_LIST)
	ResponseEntity<ApiResponse<List<UserAccessDto>, Void>> usersList(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.USERS_SET_ROLE)
	ResponseEntity<ApiResponse<UserAccessDto, Void>> setUserRole(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody UserRoleRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.RECORDS_LIST)
	ResponseEntity<ApiResponse<List<SnapshotDto>, Void>> recordsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody(required = false) RecordsRequestDto request) throws KarthiException;
}
