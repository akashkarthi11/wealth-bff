package com.wealth.bff.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;
import com.wealth.common.model.dto.customer.UserDto;

// The single Feign client of the BFF; every module adds its calls here. The caller IP goes through as X-Forwarded-For.
@FeignClient(name = "wealth-bss", url = "${wealth.bss.base-url}")
public interface WealthFeignClient {

	@GetMapping(WealthConstants.BSS_ACTUATOR_HEALTH)
	Map<String, Object> health();

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.LOGIN)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> login(
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody LoginRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.REGISTER)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> register(
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody RegisterRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.ME)
	ResponseEntity<ApiResponse<UserDto, Void>> me(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.LOGOUT)
	ResponseEntity<ApiResponse<Boolean, Void>> logout(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.PASSWORD_RESET)
	ResponseEntity<ApiResponse<Boolean, Void>> resetPassword(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody PasswordResetRequestDto request);
}
