package com.wealth.bff.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.interceptor.ApiUsageRecorder;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.UsageBatchDto;
import com.wealth.common.model.dto.admin.UsageCountDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

// Counts the BFF's own APIs (what the website calls) in memory and reports them to the BSS every 15 seconds with the shared service key, because only the BSS
// reaches Redis and the database. Counting never fails or slows a call; while the BSS cannot be reached the counts are kept (bounded) and sent later.
// Without WEALTH_SERVICE_KEY there is nobody to report to, so nothing is counted.
@Component
public class ApiUsageSender implements ApiUsageRecorder {

	private static final KarthiLogger LOGGER = new KarthiLogger(ApiUsageSender.class);
	// calls, 2xx or 3xx, 4xx, 5xx, milliseconds
	private static final int CALLS = 0;
	private static final int SUCCESS = 1;
	private static final int CLIENT = 2;
	private static final int FAILED = 3;
	private static final int MILLIS = 4;

	private final WealthFeignClient feignClient;
	private final String serviceKey;
	private final Object lock = new Object();
	private Map<String, long[]> held = new HashMap<>();
	private boolean reportFailing;

	public ApiUsageSender(WealthFeignClient feignClient, @Value("${WEALTH_SERVICE_KEY:}") String serviceKey) {
		this.feignClient = feignClient;
		this.serviceKey = serviceKey == null ? "" : serviceKey;
	}

	// The BFF API that served the request, by route template. Preflight, probes (actuator and health) and the usage screen itself are not counted.
	@Override
	public void record(String method, String route, int status, long millis) {
		try {
			if (serviceKey.isEmpty() || "OPTIONS".equalsIgnoreCase(method)
					|| (route != null && (route.startsWith("/actuator") || route.startsWith(WealthConstants.HEALTH_BASE) || route.startsWith(WealthConstants.API_USAGE_BASE)))) {
				return;
			}
			String name = route == null ? WealthConstants.USAGE_UNMATCHED : method + " " + route;
			if (name.length() > WealthConstants.USAGE_NAME_MAX) {
				name = name.substring(0, WealthConstants.USAGE_NAME_MAX);
			}
			int family = status / 100;
			String caller = RequestHeaderUtil.getUserId();
			String key = (caller == null || caller.length() != WealthConstants.USAGE_USER_ID_LENGTH ? "" : caller) + " " + name;
			synchronized (lock) {
				long[] sums = held.get(key);
				if (sums == null) {
					if (held.size() >= WealthConstants.USAGE_HELD_NAMES_MAX) {
						return;
					}
					sums = new long[5];
					held.put(key, sums);
				}
				sums[CALLS]++;
				sums[family == 4 ? CLIENT : family == 5 || status <= 0 ? FAILED : SUCCESS]++;
				sums[MILLIS] += Math.max(millis, 0);
			}
		} catch (RuntimeException e) {
			LOGGER.warn("API usage not counted: " + e.getClass().getSimpleName());
		}
	}

	@Scheduled(fixedDelay = WealthConstants.USAGE_REPORT_MILLIS)
	public void report() {
		if (serviceKey.isEmpty()) {
			return;
		}
		Map<String, long[]> taken;
		synchronized (lock) {
			if (held.isEmpty()) {
				return;
			}
			taken = held;
			held = new HashMap<>();
		}
		Map<String, long[]> sending = new HashMap<>();
		Map<String, long[]> rest = new HashMap<>();
		for (Map.Entry<String, long[]> entry : taken.entrySet()) {
			(sending.size() < WealthConstants.USAGE_REPORT_NAMES_MAX ? sending : rest).put(entry.getKey(), entry.getValue());
		}
		try {
			UsageBatchDto batch = new UsageBatchDto();
			List<UsageCountDto> counts = new ArrayList<>();
			for (Map.Entry<String, long[]> entry : sending.entrySet()) {
				UsageCountDto one = new UsageCountDto();
				int space = entry.getKey().indexOf(' ');
				one.setUserId(entry.getKey().substring(0, space));
				one.setName(entry.getKey().substring(space + 1));
				one.setCalls(entry.getValue()[CALLS]);
				one.setSuccess(entry.getValue()[SUCCESS]);
				one.setClientErrors(entry.getValue()[CLIENT]);
				one.setFailures(entry.getValue()[FAILED]);
				one.setTotalMs(entry.getValue()[MILLIS]);
				counts.add(one);
			}
			batch.setCounts(counts);
			feignClient.internalUsageRecord(serviceKey, batch);
			reportFailing = false;
			putBack(rest);
		} catch (RuntimeException e) {
			putBack(taken);
			if (!reportFailing) {
				reportFailing = true;
				LOGGER.warn("API usage could not be reported to the BSS (" + e.getClass().getSimpleName() + "); the counts are kept and sent later");
			}
		}
	}

	// Counts that were not sent are added to what came in meanwhile, so none is lost.
	private void putBack(Map<String, long[]> counts) {
		synchronized (lock) {
			for (Map.Entry<String, long[]> entry : counts.entrySet()) {
				long[] sums = held.get(entry.getKey());
				if (sums == null) {
					if (held.size() >= WealthConstants.USAGE_HELD_NAMES_MAX) {
						continue;
					}
					held.put(entry.getKey(), entry.getValue());
				} else {
					for (int i = 0; i < sums.length; i++) {
						sums[i] += entry.getValue()[i];
					}
				}
			}
		}
	}
}
