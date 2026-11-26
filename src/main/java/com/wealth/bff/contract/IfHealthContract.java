package com.wealth.bff.contract;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;

@RequestMapping(WealthConstants.HEALTH_BASE)
public interface IfHealthContract {

	// Public (interceptor): status of the BFF's link to the BSS, its database and Redis.
	@GetMapping
	ResponseEntity<Map<String, String>> health();
}
