package com.wealth.bff.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import feign.Client;
import feign.Request;
import feign.RequestTemplate;

class BssEndpointTest {

	@Test
	void theDeployedAddressIsTheDefaultAndAChangeCanBeReset() {
		BssEndpoint endpoint = new BssEndpoint("http://localhost:8081/");
		assertEquals("http://localhost:8081", endpoint.current());
		assertFalse(endpoint.isCustomised());

		endpoint.set("http://172.27.99.226:8081");
		assertEquals("http://172.27.99.226:8081", endpoint.current());
		assertTrue(endpoint.isCustomised());
		assertEquals("http://localhost:8081", endpoint.defaultUrl());

		endpoint.reset();
		assertEquals("http://localhost:8081", endpoint.current());
		assertFalse(endpoint.isCustomised());
	}

	@Test
	void settingTheDeployedAddressIsNotACustomisation() {
		BssEndpoint endpoint = new BssEndpoint("http://localhost:8081");
		endpoint.set("http://localhost:8081/");
		assertFalse(endpoint.isCustomised());
	}

	private Request request(String url) {
		Map<String, Collection<String>> headers = Map.of();
		return Request.create(Request.HttpMethod.POST, url, headers, "{}".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8, new RequestTemplate());
	}

	@SuppressWarnings("unchecked")
	private ObjectProvider<BssEndpoint> provider(BssEndpoint endpoint) {
		ObjectProvider<BssEndpoint> provider = mock(ObjectProvider.class);
		when(provider.getObject()).thenReturn(endpoint);
		return provider;
	}

	@Test
	void theClientSendsToTheChosenAddressAndKeepsThePath() throws Exception {
		BssEndpoint endpoint = new BssEndpoint("http://localhost:8081");
		endpoint.set("http://172.27.99.226:8081");
		Client delegate = mock(Client.class);
		new BssEndpointClientConfig.RewritingClient(delegate, provider(endpoint)).execute(request("http://localhost:8081/wealth-service/api/v1/accounts/list"),
				new Request.Options());
		verify(delegate).execute(argThat(r -> r.url().equals("http://172.27.99.226:8081/wealth-service/api/v1/accounts/list")), any());
	}

	@Test
	void withNoChangeTheRequestGoesOutAsIs() throws Exception {
		BssEndpoint endpoint = new BssEndpoint("http://localhost:8081");
		Client delegate = mock(Client.class);
		Request original = request("http://localhost:8081/actuator/health");
		new BssEndpointClientConfig.RewritingClient(delegate, provider(endpoint)).execute(original, new Request.Options());
		verify(delegate).execute(argThat(r -> r == original), any());
	}
}
