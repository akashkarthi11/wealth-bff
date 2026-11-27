package com.wealth.bff.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.wealth.common.model.dto.admin.UsageBatchDto;
import com.wealth.common.model.dto.admin.UsageCountDto;

class ApiUsageSenderTest {

	private WealthFeignClient feign;
	private ApiUsageSender sender;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		sender = new ApiUsageSender(feign, "the-service-key");
	}

	private UsageCountDto find(List<UsageCountDto> counts, String name) {
		return counts.stream().filter(c -> c.getName().equals(name)).findFirst().orElseThrow();
	}

	private UsageBatchDto reported() {
		ArgumentCaptor<UsageBatchDto> batch = ArgumentCaptor.forClass(UsageBatchDto.class);
		verify(feign).internalUsageRecord(eq("the-service-key"), batch.capture());
		return batch.getValue();
	}

	@Test
	void everyFinishedRequestIsCountedByRouteTemplateAndReportedOnceWithTheServiceKey() {
		sender.record("POST", "/wealth/api/v1/accounts/list", 200, 10);
		sender.record("POST", "/wealth/api/v1/accounts/list", 404, 20);
		sender.record("POST", "/wealth/api/v1/accounts/list", 502, 30);
		sender.record("POST", "/wealth/api/v1/accounts/list", 302, 0);

		sender.report();
		sender.report();

		UsageCountDto one = find(reported().getCounts(), "POST /wealth/api/v1/accounts/list");
		assertEquals(4, one.getCalls());
		assertEquals(2, one.getSuccess());
		assertEquals(1, one.getClientErrors());
		assertEquals(1, one.getFailures());
		assertEquals(60, one.getTotalMs());
		verify(feign, times(1)).internalUsageRecord(any(), any());
	}

	@Test
	void preflightProbesAndTheUsageScreenAreNotCountedAndAnUnknownPathIsOneName() {
		sender.record("OPTIONS", "/wealth/api/v1/accounts/list", 200, 1);
		sender.record("GET", "/actuator/health", 200, 1);
		sender.record("GET", "/wealth/api/v1/health", 200, 1);
		sender.record("POST", "/wealth/api/v1/advisor/api-usage/list", 200, 1);
		sender.record("GET", null, 404, 1);

		sender.report();

		List<UsageCountDto> counts = reported().getCounts();
		assertEquals(1, counts.size());
		assertEquals("UNMATCHED", counts.get(0).getName());
	}

	@Test
	void whenTheBssCannotBeReachedTheCountsAreKeptAndSentLaterWithNewOnes() {
		sender.record("POST", "/a", 200, 10);
		when(feign.internalUsageRecord(any(), any())).thenThrow(new IllegalStateException("bss down"));
		sender.report();

		sender.record("POST", "/a", 200, 20);
		org.mockito.Mockito.reset(feign);
		sender.report();

		UsageCountDto one = find(reported().getCounts(), "POST /a");
		assertEquals(2, one.getCalls(), "the count that failed to send is not lost");
		assertEquals(30, one.getTotalMs());
	}

	@Test
	void aReportHasAtMostFiveHundredNamesAndTheRestIsSentNextTime() {
		for (int i = 0; i < 600; i++) {
			sender.record("GET", "/r" + i, 200, 1);
		}

		sender.report();
		sender.report();

		ArgumentCaptor<UsageBatchDto> batch = ArgumentCaptor.forClass(UsageBatchDto.class);
		verify(feign, times(2)).internalUsageRecord(eq("the-service-key"), batch.capture());
		assertEquals(500, batch.getAllValues().get(0).getCounts().size());
		assertEquals(100, batch.getAllValues().get(1).getCounts().size());
	}

	@Test
	void withoutAServiceKeyNothingIsCountedOrSent() {
		ApiUsageSender noKey = new ApiUsageSender(feign, "");
		noKey.record("POST", "/a", 200, 10);

		noKey.report();

		verify(feign, never()).internalUsageRecord(any(), any());
		assertTrue(true);
	}

	@Test
	void countingNeverThrowsWhateverItIsGiven() {
		sender.record(null, null, 0, -5);
		sender.record("GET", "x".repeat(500), 999, Long.MAX_VALUE);
	}

	@Test
	void eachCountIsReportedWithTheUserWhoMadeTheCallsAndAnUnknownUserIsLeftEmpty() {
		String me = "11111111-1111-1111-1111-111111111111";
		com.wealth.common.utils.generic.RequestHeaderUtil.setUserId(me);
		sender.record("GET", "/a", 200, 5);
		sender.record("GET", "/a", 200, 5);
		com.wealth.common.utils.generic.RequestHeaderUtil.setUserId("short");
		sender.record("GET", "/a", 200, 5);
		com.wealth.common.utils.generic.RequestHeaderUtil.clear();
		sender.record("GET", "/a", 200, 5);

		sender.report();

		List<UsageCountDto> counts = reported().getCounts();
		assertEquals(2, counts.size(), "one row for me, one for no user");
		UsageCountDto mine = counts.stream().filter(c -> me.equals(c.getUserId())).findFirst().orElseThrow();
		assertEquals(2, mine.getCalls());
		assertEquals("/a", mine.getName().substring(mine.getName().indexOf('/')));
		UsageCountDto nobody = counts.stream().filter(c -> "".equals(c.getUserId())).findFirst().orElseThrow();
		assertEquals(2, nobody.getCalls(), "the short id and the missing id are both no user");
	}
}
