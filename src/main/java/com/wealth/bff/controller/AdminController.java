package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfAdminContract;
import com.wealth.bff.validator.AdminValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.RecordsRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
import com.wealth.common.model.dto.admin.RoleMenusRequestDto;
import com.wealth.common.model.dto.admin.RolesOverviewDto;
import com.wealth.common.model.dto.admin.SnapshotDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class AdminController extends WealthBaseController implements IfAdminContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(AdminController.class);

	private final WealthFeignClient feignClient;

	public AdminController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<RolesOverviewDto, Void>> rolesList(String authorization) throws KarthiException {
		LOGGER.info("Entering rolesList BFF");
		return handleResponse(feignClient.adminRolesList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<RoleMenusDto, Void>> updateRoleMenus(String authorization, RoleMenusRequestDto request)
			throws KarthiException {
		LOGGER.info("Entering updateRoleMenus BFF");
		AdminValidator.validateRoleMenus(request);
		return handleResponse(feignClient.adminUpdateRoleMenus(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<SnapshotDto>, Void>> recordsList(String authorization, RecordsRequestDto request)
			throws KarthiException {
		LOGGER.info("Entering recordsList BFF");
		AdminValidator.validateRecords(request);
		return handleResponse(feignClient.adminRecordsList(authorization, RequestHeaderUtil.getIp(), request));
	}
}
