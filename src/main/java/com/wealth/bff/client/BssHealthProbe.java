package com.wealth.bff.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.logger.KarthiLogger;

// Asks a BSS address for its health, so an address is only used once it answers UP.
@Component
public class BssHealthProbe {

	private static final KarthiLogger LOGGER = new KarthiLogger(BssHealthProbe.class);

	private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(WealthConstants.CONNECTION_PROBE_TIMEOUT_SECONDS)).build();
	private final ObjectMapper mapper = new ObjectMapper();

	public boolean isUp(String baseUrl) {
		try {
			HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + WealthConstants.BSS_ACTUATOR_HEALTH))
					.timeout(Duration.ofSeconds(WealthConstants.CONNECTION_PROBE_TIMEOUT_SECONDS)).GET().build();
			HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
			JsonNode status = mapper.readTree(response.body()).get(WealthConstants.KEY_STATUS);
			return response.statusCode() == 200 && status != null && WealthConstants.STATUS_UP.equals(status.asText());
		} catch (Exception e) {
			LOGGER.warn("BSS health probe failed");
			return false;
		}
	}
}
