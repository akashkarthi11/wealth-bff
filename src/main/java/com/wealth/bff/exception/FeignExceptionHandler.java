package com.wealth.bff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.logger.KarthiLogger;

import feign.FeignException;

// The BSS interceptor answers 401/403 with an empty body; pass the status on with an error code, never the detail.
@RestControllerAdvice
public class FeignExceptionHandler {

	private static final KarthiLogger LOGGER = new KarthiLogger(FeignExceptionHandler.class);

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ApiResponse<Void, Void>> handleFeignException(FeignException ex) {
		int status = ex.status();
		LOGGER.warn("BSS call failed | status=" + status);
		boolean denied = status == HttpStatus.UNAUTHORIZED.value() || status == HttpStatus.FORBIDDEN.value();
		ApiResponse<Void, Void> response = new ApiResponse<>();
		response.setStatus(0);
		response.setErrorCode(denied ? BaseError.UNAUTHORIZED : BaseError.GENERIC_EXCEPTION);
		response.setErrorMessage(BaseError.getConstantNameByValue(response.getErrorCode()));
		HttpStatus out = denied ? HttpStatus.valueOf(status) : HttpStatus.BAD_GATEWAY;
		return ResponseEntity.status(out).body(response);
	}
}
