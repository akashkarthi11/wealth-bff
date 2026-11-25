package com.wealth.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;
import com.wealth.common.model.dto.customer.UserDto;

// The single Feign client of the BFF; every module adds its calls here. The caller IP goes through as X-Forwarded-For.
@FeignClient(name = "wealth-bss", url = "${wealth.bss.base-url}")
public interface WealthFeignClient {

	@PostMapping("/wealth-service/api/v1/customers/users/login")
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> login(
			@RequestHeader(value = "X-Forwarded-For", required = false) String ip, @RequestBody LoginRequestDto request);

	@PostMapping("/wealth-service/api/v1/customers/users/register")
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> register(
			@RequestHeader(value = "X-Forwarded-For", required = false) String ip,
			@RequestBody RegisterRequestDto request);

	@PostMapping("/wealth-service/api/v1/customers/users/me")
	ResponseEntity<ApiResponse<UserDto, Void>> me(@RequestHeader("Authorization") String authorization,
			@RequestHeader(value = "X-Forwarded-For", required = false) String ip);

	@PostMapping("/wealth-service/api/v1/customers/users/logout")
	ResponseEntity<ApiResponse<Boolean, Void>> logout(@RequestHeader("Authorization") String authorization,
			@RequestHeader(value = "X-Forwarded-For", required = false) String ip);

	@PostMapping("/wealth-service/api/v1/customers/users/password/reset")
	ResponseEntity<ApiResponse<Boolean, Void>> resetPassword(@RequestHeader("Authorization") String authorization,
			@RequestHeader(value = "X-Forwarded-For", required = false) String ip,
			@RequestBody PasswordResetRequestDto request);
}
