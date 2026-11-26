package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.BssEndpoint;
import com.wealth.bff.client.BssHealthProbe;
import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.constants.WealthConstants;
import com.wealth.bff.contract.IfConnectionsContract;
import com.wealth.bff.validator.ConnectionsValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.ApiResponseTemplate;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;
import com.wealth.common.model.dto.customer.UserDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class ConnectionsController implements IfConnectionsContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(ConnectionsController.class);

	private final WealthFeignClient feignClient;
	private final BssEndpoint bssEndpoint;
	private final BssHealthProbe probe;
	private final ApiResponseTemplate responseTemplate;

	public ConnectionsController(WealthFeignClient feignClient, BssEndpoint bssEndpoint, BssHealthProbe probe, ApiResponseTemplate responseTemplate) {
		this.feignClient = feignClient;
		this.bssEndpoint = bssEndpoint;
		this.probe = probe;
		this.responseTemplate = responseTemplate;
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> bssGet(String authorization) throws KarthiException {
		LOGGER.info("Entering bssGet BFF");
		requireAdmin(authorization);
		return responseTemplate.apires(describe(), BaseError.Success);
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> bssUpdate(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering bssUpdate BFF");
		requireAdmin(authorization);
		String url = ConnectionsValidator.validateUrl(request);
		if (!probe.isUp(url)) {
			throw new KarthiException(BaseError.CONNECTION_NOT_UP);
		}
		saveToTable(authorization, url);
		bssEndpoint.set(url);
		LOGGER.info("BSS address changed by an admin");
		return responseTemplate.apires(describe(), BaseError.Success);
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> bssReset(String authorization) throws KarthiException {
		LOGGER.info("Entering bssReset BFF");
		requireAdmin(authorization);
		saveToTable(authorization, bssEndpoint.defaultUrl());
		bssEndpoint.reset();
		LOGGER.info("BSS address reset to the deployed one by an admin");
		return responseTemplate.apires(describe(), BaseError.Success);
	}

	// The table is the source of truth: the address is saved there (through the BSS) before it is used.
	private void saveToTable(String authorization, String url) {
		ConnectionRequestDto save = new ConnectionRequestDto();
		save.setKey(WealthConstants.SETTING_BFF_TO_BSS);
		save.setUrl(url);
		ResponseEntity<ApiResponse<ConnectionDto, Void>> saved = feignClient.environmentAddressSave(authorization, RequestHeaderUtil.getIp(), save);
		ApiResponse<ConnectionDto, Void> body = saved == null ? null : saved.getBody();
		if (body == null || body.getStatus() != 1) {
			throw new KarthiException(body == null || body.getErrorCode() == null ? BaseError.GENERIC_EXCEPTION : body.getErrorCode());
		}
	}

	// The BSS knows the token and the role; the BFF only asks it who is calling.
	private void requireAdmin(String authorization) {
		ResponseEntity<ApiResponse<UserDto, Void>> me = feignClient.me(authorization, RequestHeaderUtil.getIp());
		UserDto user = me.getBody() == null ? null : me.getBody().getData();
		if (user == null || !WealthConstants.ROLE_ADMIN.equals(user.getRole())) {
			throw new KarthiException(BaseError.ADMIN_ONLY_ACTION);
		}
	}

	private ConnectionDto describe() {
		ConnectionDto dto = new ConnectionDto();
		dto.setUrl(bssEndpoint.current());
		dto.setDefaultUrl(bssEndpoint.defaultUrl());
		dto.setCustomised(bssEndpoint.isCustomised());
		dto.setStatus(probe.isUp(dto.getUrl()) ? WealthConstants.STATUS_UP : WealthConstants.STATUS_DOWN);
		return dto;
	}
}
