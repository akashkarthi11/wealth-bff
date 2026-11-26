package com.wealth.bff.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.admin.ConnectionDto;

class BssAddressBootTest {

	private WealthFeignClient feign;
	private BssEndpoint endpoint;
	private BssHealthProbe probe;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		endpoint = new BssEndpoint("http://localhost:8081");
		probe = mock(BssHealthProbe.class);
	}

	private void tableSays(String url) {
		ConnectionDto dto = new ConnectionDto();
		dto.setUrl(url);
		ApiResponse<ConnectionDto, Void> body = new ApiResponse<>();
		body.setData(dto);
		when(feign.internalSettingsPick(eq("key"), any())).thenReturn(ResponseEntity.ok(body));
	}

	@Test
	void atBootTheSavedAddressIsPickedAndHeldWhenItAnswers() {
		tableSays("http://172.27.99.226:8081");
		when(probe.isUp("http://172.27.99.226:8081")).thenReturn(true);

		new BssAddressBoot(feign, endpoint, probe, "key").pickAtBoot();

		assertEquals("http://172.27.99.226:8081", endpoint.current());
		verify(feign).internalSettingsPick(eq("key"), argThat(r -> "bff_to_bss".equals(r.getKey()) && "http://localhost:8081".equals(r.getUrl())));
	}

	@Test
	void aSavedAddressThatDoesNotAnswerIsNotUsed() {
		tableSays("http://dead.example.com:8081");
		when(probe.isUp("http://dead.example.com:8081")).thenReturn(false);

		new BssAddressBoot(feign, endpoint, probe, "key").pickAtBoot();

		assertEquals("http://localhost:8081", endpoint.current());
		assertFalse(endpoint.isCustomised());
	}

	@Test
	void withoutTheServiceKeyNothingIsAskedAndTheDeployedAddressStays() {
		new BssAddressBoot(feign, endpoint, probe, "").pickAtBoot();
		verify(feign, never()).internalSettingsPick(any(), any());
		assertEquals("http://localhost:8081", endpoint.current());
	}

	@Test
	void ifTheBssCannotBeReachedBootContinuesWithTheDeployedAddress() {
		when(feign.internalSettingsPick(any(), any())).thenThrow(new IllegalStateException("down"));
		new BssAddressBoot(feign, endpoint, probe, "key").pickAtBoot();
		assertEquals("http://localhost:8081", endpoint.current());
	}
}
