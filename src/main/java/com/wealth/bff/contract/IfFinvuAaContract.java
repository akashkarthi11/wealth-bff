package com.wealth.bff.contract;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;

@RequestMapping(WealthConstants.FINVU_AA_BASE)
public interface IfFinvuAaContract {
	@PostMapping(WealthConstants.FINVU_AA_CONFIG)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> configuration(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
	@PostMapping(WealthConstants.FINVU_AA_CONSENT_REQUEST)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> requestConsent(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody Map<String, Object> request) throws KarthiException;
	@PostMapping(WealthConstants.FINVU_AA_CONSENT_STATUS)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> consentStatus(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody Map<String, Object> request) throws KarthiException;
	@PostMapping(WealthConstants.FINVU_AA_FI_REQUEST)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> requestFinancialInformation(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody Map<String, Object> request) throws KarthiException;
	@PostMapping(WealthConstants.FINVU_AA_FI_STATUS)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> financialInformationStatus(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody Map<String, Object> request) throws KarthiException;
	@PostMapping(WealthConstants.FINVU_AA_FI_FETCH)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> fetchFinancialInformation(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody Map<String, Object> request) throws KarthiException;
}
