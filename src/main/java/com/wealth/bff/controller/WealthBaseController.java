package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;

import com.wealth.common.generic.ApiResponse;

// What every BFF controller shares: the BSS answer is passed on with its own status and body.
public abstract class WealthBaseController {

	protected static <T> ResponseEntity<ApiResponse<T, Void>> handleResponse(ResponseEntity<ApiResponse<T, Void>> response) {
		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}
}
