package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfEnvironmentContract;
import com.wealth.bff.validator.EnvironmentValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class EnvironmentController extends WealthBaseController implements IfEnvironmentContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(EnvironmentController.class);

	private final WealthFeignClient feignClient;

	public EnvironmentController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> redisGet(String authorization) throws KarthiException {
		LOGGER.info("Entering redisGet BFF");
		return handleResponse(feignClient.environmentRedisGet(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> redisUpdate(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering redisUpdate BFF");
		EnvironmentValidator.validateAddress(request);
		return handleResponse(feignClient.environmentRedisUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> redisReset(String authorization) throws KarthiException {
		LOGGER.info("Entering redisReset BFF");
		return handleResponse(feignClient.environmentRedisReset(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> redisActive(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering redisActive BFF");
		EnvironmentValidator.validateActive(request);
		return handleResponse(feignClient.environmentRedisActive(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaActive(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering kafkaActive BFF");
		EnvironmentValidator.validateActive(request);
		return handleResponse(feignClient.environmentKafkaActive(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaRefreshTime(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering kafkaRefreshTime BFF");
		EnvironmentValidator.validateRefreshTime(request);
		return handleResponse(feignClient.environmentKafkaRefreshTime(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaGet(String authorization) throws KarthiException {
		LOGGER.info("Entering kafkaGet BFF");
		return handleResponse(feignClient.environmentKafkaGet(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaUpdate(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering kafkaUpdate BFF");
		EnvironmentValidator.validateAddress(request);
		return handleResponse(feignClient.environmentKafkaUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> kafkaReset(String authorization) throws KarthiException {
		LOGGER.info("Entering kafkaReset BFF");
		return handleResponse(feignClient.environmentKafkaReset(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresGet(String authorization) throws KarthiException {
		LOGGER.info("Entering postgresGet BFF");
		return handleResponse(feignClient.environmentPostgresGet(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresUpdate(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering postgresUpdate BFF");
		EnvironmentValidator.validateAddress(request);
		return handleResponse(feignClient.environmentPostgresUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> postgresReset(String authorization) throws KarthiException {
		LOGGER.info("Entering postgresReset BFF");
		return handleResponse(feignClient.environmentPostgresReset(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> addressSave(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering addressSave BFF");
		EnvironmentValidator.validateServiceAddress(request);
		return handleResponse(feignClient.environmentAddressSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> signupGet(String authorization) throws KarthiException {
		LOGGER.info("Entering signupGet BFF");
		return handleResponse(feignClient.environmentSignupGet(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> signupUpdate(String authorization, ConnectionRequestDto request) throws KarthiException {
		LOGGER.info("Entering signupUpdate BFF");
		EnvironmentValidator.validateAccessCode(request);
		return handleResponse(feignClient.environmentSignupUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> signupClose(String authorization) throws KarthiException {
		LOGGER.info("Entering signupClose BFF");
		return handleResponse(feignClient.environmentSignupClose(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<ConnectionDto, Void>> signupReset(String authorization) throws KarthiException {
		LOGGER.info("Entering signupReset BFF");
		return handleResponse(feignClient.environmentSignupReset(authorization, RequestHeaderUtil.getIp()));
	}
}
