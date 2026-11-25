package com.wealth.bff.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;

import feign.FeignException;

class FeignExceptionHandlerTest {

	private final FeignExceptionHandler handler = new FeignExceptionHandler();

	private ResponseEntity<ApiResponse<Void, Void>> handle(int status) {
		FeignException ex = mock(FeignException.class);
		when(ex.status()).thenReturn(status);
		return handler.handleFeignException(ex);
	}

	@Test
	void unauthorizedAndForbiddenKeepTheirStatusWithTheUnauthorizedCode() {
		assertEquals(HttpStatus.UNAUTHORIZED, handle(401).getStatusCode());
		assertEquals(HttpStatus.FORBIDDEN, handle(403).getStatusCode());
		assertEquals(BaseError.UNAUTHORIZED, handle(401).getBody().getErrorCode());
		assertEquals(0, handle(401).getBody().getStatus());
	}

	@Test
	void anythingElseBecomesABadGatewayWithoutDetail() {
		ResponseEntity<ApiResponse<Void, Void>> out = handle(500);
		assertEquals(HttpStatus.BAD_GATEWAY, out.getStatusCode());
		assertEquals(BaseError.GENERIC_EXCEPTION, out.getBody().getErrorCode());
		assertEquals(HttpStatus.BAD_GATEWAY, handle(-1).getStatusCode());
	}
}
