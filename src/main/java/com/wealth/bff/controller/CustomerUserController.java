package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfCustomerUserContract;
import com.wealth.bff.validator.CustomerUserValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;
import com.wealth.common.model.dto.customer.UserDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class CustomerUserController implements IfCustomerUserContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(CustomerUserController.class);

	private final WealthFeignClient feignClient;

	public CustomerUserController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<LoginResponseDto, Void>> login(LoginRequestDto request) throws KarthiException {
		LOGGER.info("Entering login BFF");
		CustomerUserValidator.validateLogin(request);
		return handleResponse(feignClient.login(RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<LoginResponseDto, Void>> register(RegisterRequestDto request)
			throws KarthiException {
		LOGGER.info("Entering register BFF");
		CustomerUserValidator.validateRegister(request);
		return handleResponse(feignClient.register(RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<UserDto, Void>> me(String authorization) throws KarthiException {
		LOGGER.info("Entering me BFF");
		return handleResponse(feignClient.me(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> logout(String authorization) throws KarthiException {
		LOGGER.info("Entering logout BFF");
		return handleResponse(feignClient.logout(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> resetPassword(String authorization,
			PasswordResetRequestDto request) throws KarthiException {
		LOGGER.info("Entering resetPassword BFF");
		CustomerUserValidator.validatePasswordReset(request);
		return handleResponse(feignClient.resetPassword(authorization, RequestHeaderUtil.getIp(), request));
	}

	private static <T> ResponseEntity<ApiResponse<T, Void>> handleResponse(
			ResponseEntity<ApiResponse<T, Void>> response) {
		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}
}
