package com.wealth.bff.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

// Boot: the table wealth_settings is the source of truth for the BSS address. The BFF asks the BSS (at the deployed address, with the shared service
// key WEALTH_SERVICE_KEY) for the saved address, holds it in memory, and saves the deployed one to the table when there is none yet. A saved address
// is used only when it answers, so a wrong row can never cut the BFF off from the BSS.
@Component
public class BssAddressBoot {

	private static final KarthiLogger LOGGER = new KarthiLogger(BssAddressBoot.class);

	private final WealthFeignClient feignClient;
	private final BssEndpoint bssEndpoint;
	private final BssHealthProbe probe;
	private final String serviceKey;

	public BssAddressBoot(WealthFeignClient feignClient, BssEndpoint bssEndpoint, BssHealthProbe probe, @Value("${WEALTH_SERVICE_KEY:}") String serviceKey) {
		this.feignClient = feignClient;
		this.bssEndpoint = bssEndpoint;
		this.probe = probe;
		this.serviceKey = serviceKey == null ? "" : serviceKey;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void pickAtBoot() {
		if (serviceKey.isEmpty()) {
			LOGGER.info("WEALTH_SERVICE_KEY is not set; the deployed BSS address is used");
			return;
		}
		try {
			ConnectionRequestDto request = new ConnectionRequestDto();
			request.setKey(WealthConstants.SETTING_BFF_TO_BSS);
			request.setUrl(bssEndpoint.defaultUrl());
			ResponseEntity<ApiResponse<ConnectionDto, Void>> answer = feignClient.internalSettingsPick(serviceKey, request);
			ConnectionDto saved = answer.getBody() == null ? null : answer.getBody().getData();
			if (saved == null || saved.getUrl() == null || saved.getUrl().equals(bssEndpoint.current())) {
				return;
			}
			if (probe.isUp(saved.getUrl())) {
				bssEndpoint.set(saved.getUrl());
				LOGGER.info("BSS address picked from wealth_settings");
			} else {
				LOGGER.warn("The BSS address saved in wealth_settings does not answer; the deployed one is used");
			}
		} catch (RuntimeException e) {
			LOGGER.warn("The BSS address could not be read from wealth_settings; the deployed one is used: " + e.getClass().getSimpleName());
		}
	}
}
