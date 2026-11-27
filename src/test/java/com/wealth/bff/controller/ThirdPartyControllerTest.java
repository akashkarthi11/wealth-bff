package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartyDto;
import com.wealth.common.model.dto.admin.ThirdPartySaveRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class ThirdPartyControllerTest {

	private WealthFeignClient feign;
	private ThirdPartyController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new ThirdPartyController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private ThirdPartySaveRequestDto save(String provider, String key) {
		ThirdPartySaveRequestDto r = new ThirdPartySaveRequestDto();
		r.setProvider(provider);
		r.setLabel("Label");
		r.setBaseUrl("https://api.example.com");
		r.setApiKey(key);
		r.setPriority(1);
		return r;
	}

	private ProviderRequestDto provider(String key) {
		ProviderRequestDto r = new ProviderRequestDto();
		r.setProvider(key);
		return r;
	}

	@Test
	void theOverviewIsForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<com.wealth.common.model.dto.admin.ConnectionsOverviewDto, Void> body = new ApiResponse<>();
		when(feign.thirdPartyOverview("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.overview("Bearer t").getBody());
	}

	@Test
	void theListIsForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<List<ThirdPartyDto>, Void> body = new ApiResponse<>();
		when(feign.thirdPartyList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.list("Bearer t").getBody());
	}

	@Test
	void aSaveIsCheckedAtTheEdgeThenForwarded() {
		ThirdPartySaveRequestDto ok = save("gemini", "secret");
		ApiResponse<ThirdPartyDto, Void> body = new ApiResponse<>();
		when(feign.thirdPartySave("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.save("Bearer t", ok).getBody());

		assertEquals(BaseError.PROVIDER_INVALID, assertThrows(KarthiException.class, () -> controller.save("Bearer t", save("Bad Name", null))).getMessage());
		assertEquals(BaseError.PROVIDER_INVALID, assertThrows(KarthiException.class, () -> controller.save("Bearer t", null)).getMessage());
		assertEquals(BaseError.ENVIRONMENT_CREDENTIALS_INVALID,
				assertThrows(KarthiException.class, () -> controller.save("Bearer t", save("gemini", "k".repeat(501)))).getMessage());
		ThirdPartySaveRequestDto longLabel = save("gemini", null);
		longLabel.setLabel("x".repeat(61));
		assertThrows(KarthiException.class, () -> controller.save("Bearer t", longLabel));
		verify(feign, never()).thirdPartySave(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && "Bad Name".equals(r.getProvider())));
	}

	@Test
	void testAndRemoveNeedAProviderKeyAndAreForwarded() {
		ProviderRequestDto ok = provider("gemini");
		ApiResponse<ThirdPartyDto, Void> tested = new ApiResponse<>();
		ApiResponse<Boolean, Void> removed = new ApiResponse<>();
		when(feign.thirdPartyTest("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(tested));
		when(feign.thirdPartyRemove("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(removed));

		assertEquals(tested, controller.test("Bearer t", ok).getBody());
		assertEquals(removed, controller.remove("Bearer t", ok).getBody());
		assertThrows(KarthiException.class, () -> controller.test("Bearer t", provider("1; drop")));
		assertThrows(KarthiException.class, () -> controller.remove("Bearer t", provider(null)));
		assertThrows(KarthiException.class, () -> controller.remove("Bearer t", null));
	}

	@Test
	void aFetchNeedsAProviderKeyAndIsForwarded() {
		ProviderRequestDto ok = provider("angel-one");
		ApiResponse<ThirdPartyDto, Void> fetched = new ApiResponse<>();
		when(feign.thirdPartyFetch("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(fetched));

		assertEquals(fetched, controller.fetch("Bearer t", ok).getBody());
		assertThrows(KarthiException.class, () -> controller.fetch("Bearer t", provider("1; drop")));
		assertThrows(KarthiException.class, () -> controller.fetch("Bearer t", null));
		verify(feign, never()).thirdPartyFetch(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && "1; drop".equals(r.getProvider())));
	}

	@Test
	void loginValuesAreCheckedForSizeAtTheEdge() {
		ThirdPartySaveRequestDto ok = save("angel-one", "k");
		ok.setExtra(java.util.Map.of("pin", "1234"));
		ApiResponse<ThirdPartyDto, Void> body = new ApiResponse<>();
		when(feign.thirdPartySave("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.save("Bearer t", ok).getBody());

		ThirdPartySaveRequestDto big = save("angel-one", "k");
		big.setExtra(java.util.Map.of("pin", "x".repeat(201)));
		assertEquals(BaseError.ENVIRONMENT_CREDENTIALS_INVALID, assertThrows(KarthiException.class, () -> controller.save("Bearer t", big)).getMessage());
		ThirdPartySaveRequestDto many = save("angel-one", "k");
		java.util.Map<String, String> entries = new java.util.HashMap<>();
		for (int i = 0; i < 11; i++) {
			entries.put("k" + i, "v");
		}
		many.setExtra(entries);
		assertEquals(BaseError.ENVIRONMENT_CREDENTIALS_INVALID, assertThrows(KarthiException.class, () -> controller.save("Bearer t", many)).getMessage());
	}
}
