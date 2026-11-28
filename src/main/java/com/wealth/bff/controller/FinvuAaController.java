package com.wealth.bff.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfFinvuAaContract;
import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.ApiResponseTemplate;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class FinvuAaController implements IfFinvuAaContract {
	private final WealthFeignClient client;
	private final ApiResponseTemplate responseTemplate;

	public FinvuAaController(WealthFeignClient client, ApiResponseTemplate responseTemplate) {
		this.client = client;
		this.responseTemplate = responseTemplate;
	}

	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> configuration(String authorization) throws KarthiException {
		return responseTemplate.apires(data(client.finvuConfiguration(authorization, RequestHeaderUtil.getIp())), BaseError.Success);
	}
	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> requestConsent(String authorization, Map<String, Object> request) throws KarthiException {
		return responseTemplate.apires(data(client.finvuConsentRequest(authorization, RequestHeaderUtil.getIp(), request)), BaseError.Success);
	}
	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> consentStatus(String authorization, Map<String, Object> request) throws KarthiException {
		return responseTemplate.apires(data(client.finvuConsentStatus(authorization, RequestHeaderUtil.getIp(), request)), BaseError.Success);
	}
	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> requestFinancialInformation(String authorization, Map<String, Object> request) throws KarthiException {
		return responseTemplate.apires(data(client.finvuFiRequest(authorization, RequestHeaderUtil.getIp(), request)), BaseError.Success);
	}
	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> financialInformationStatus(String authorization, Map<String, Object> request) throws KarthiException {
		return responseTemplate.apires(data(client.finvuFiStatus(authorization, RequestHeaderUtil.getIp(), request)), BaseError.Success);
	}
	@Override public ResponseEntity<ApiResponse<Map<String, Object>, Void>> fetchFinancialInformation(String authorization, Map<String, Object> request) throws KarthiException {
		return responseTemplate.apires(data(client.finvuFiFetch(authorization, RequestHeaderUtil.getIp(), request)), BaseError.Success);
	}

	private Map<String, Object> data(ResponseEntity<ApiResponse<Map<String, Object>, Void>> response) {
		if (response == null || response.getBody() == null || response.getBody().getStatus() != 1) {
			throw new KarthiException(response == null || response.getBody() == null || response.getBody().getErrorCode() == null
					? BaseError.GENERIC_EXCEPTION : response.getBody().getErrorCode());
		}
		return response.getBody().getData();
	}
}
