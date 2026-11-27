package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

// Environment settings (Redis and Kafka addresses). Admin only, decided in the BSS, which owns both.
@RequestMapping(WealthConstants.ENVIRONMENT_BASE)
public interface IfEnvironmentContract {

	@PostMapping(WealthConstants.ENV_REDIS_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> redisGet(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_REDIS_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> redisUpdate(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_REDIS_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> redisReset(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	// The Active / Inactive switch for Redis and Kafka (request.enabled). Decided in the BSS.
	@PostMapping(WealthConstants.ENV_REDIS_ACTIVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> redisActive(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_KAFKA_ACTIVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaActive(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_KAFKA_REFRESH)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaRefreshTime(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_KAFKA_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaGet(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_KAFKA_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaUpdate(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_KAFKA_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaReset(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_POSTGRES_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresGet(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_POSTGRES_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresUpdate(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_POSTGRES_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresReset(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	// Admin: saves an address in the table (the UI uses this for its own address). Decided in the BSS.
	@PostMapping(WealthConstants.ENV_ADDRESS_SAVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> addressSave(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	// Admin: the sign-up access code. get shows the state only (never the code); update saves a new code; close stops sign-up; reset goes back to the deployed key.
	@PostMapping(WealthConstants.ENV_SIGNUP_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> signupGet(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_SIGNUP_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> signupUpdate(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ConnectionRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.ENV_SIGNUP_CLOSE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> signupClose(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.ENV_SIGNUP_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> signupReset(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;
}
