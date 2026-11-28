package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.transaction.TransactionCreateRequestDto;
import com.wealth.common.model.dto.transaction.TransactionDto;
import com.wealth.common.model.dto.transaction.TransactionPageDto;
import com.wealth.common.model.dto.transaction.TransactionPageRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class TransactionsControllerTest {

	private WealthFeignClient feign;
	private TransactionsController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new TransactionsController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	@Test
	void theListRefreshCreateAndDeleteAreForwardedWithTheTokenAndTheCallerIp() {
		TransactionPageRequestDto page = new TransactionPageRequestDto();
		page.setSize(50);
		ApiResponse<TransactionPageDto, Void> pageBody = new ApiResponse<>();
		when(feign.transactionsList("Bearer t", "203.0.113.5", page)).thenReturn(ResponseEntity.ok(pageBody));
		assertEquals(pageBody, controller.list("Bearer t", page).getBody());
		when(feign.transactionsRefresh("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(pageBody));
		assertEquals(pageBody, controller.refresh("Bearer t").getBody());

		TransactionCreateRequestDto entry = new TransactionCreateRequestDto();
		entry.setAmount(new BigDecimal("10"));
		ApiResponse<TransactionDto, Void> entryBody = new ApiResponse<>();
		when(feign.transactionsCreate("Bearer t", "203.0.113.5", entry)).thenReturn(ResponseEntity.ok(entryBody));
		assertEquals(entryBody, controller.create("Bearer t", entry).getBody());

		IdRequestDto id = new IdRequestDto();
		id.setId("11111111-2222-3333-4444-555555555555");
		ApiResponse<Boolean, Void> removed = new ApiResponse<>();
		when(feign.transactionsDelete("Bearer t", "203.0.113.5", id)).thenReturn(ResponseEntity.ok(removed));
		assertEquals(removed, controller.delete("Bearer t", id).getBody());
	}

	@Test
	void theSummaryIsForwardedWithTheTokenAndTheCallerIpAndABadFilterNeverReachesTheBss() {
		TransactionPageRequestDto filter = new TransactionPageRequestDto();
		filter.setKind("expense");
		filter.setFrom("2026-11-01");
		ApiResponse<com.wealth.common.model.dto.transaction.TransactionSummaryDto, Void> body = new ApiResponse<>();
		when(feign.transactionsSummary("Bearer t", "203.0.113.5", filter)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.summary("Bearer t", filter).getBody());

		when(feign.transactionsSummary(any(), any(), any())).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.summary("Bearer t", null).getBody());

		TransactionPageRequestDto longSearch = new TransactionPageRequestDto();
		longSearch.setQ("x".repeat(101));
		TransactionPageRequestDto longKind = new TransactionPageRequestDto();
		longKind.setKind("k".repeat(21));
		for (TransactionPageRequestDto bad : new TransactionPageRequestDto[] { longSearch, longKind }) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.summary("Bearer t", bad)).getMessage());
		}
		verify(feign, org.mockito.Mockito.times(2)).transactionsSummary(any(), any(), any());
	}

	@Test
	void theSummaryKeepsTheKindOfEachCategoryAndTheTotalsByKindAsTheBssSentThem() throws Exception {
		com.wealth.common.model.dto.transaction.TransactionSummaryItemDto category = new com.wealth.common.model.dto.transaction.TransactionSummaryItemDto();
		category.setLabel("Crypto");
		category.setKind("investment");
		com.wealth.common.model.dto.transaction.TransactionSummaryItemDto kind = new com.wealth.common.model.dto.transaction.TransactionSummaryItemDto();
		kind.setLabel("investment");
		kind.setKind("investment");
		com.wealth.common.model.dto.transaction.TransactionSummaryDto summary = new com.wealth.common.model.dto.transaction.TransactionSummaryDto();
		category.setTrend(java.util.List.of(new java.math.BigDecimal("10.00"), new java.math.BigDecimal("0.00")));
		com.wealth.common.model.dto.transaction.TransactionSummaryItemDto day = new com.wealth.common.model.dto.transaction.TransactionSummaryItemDto();
		day.setLabel("2026-11-03");
		summary.setByCategory(java.util.List.of(category));
		summary.setByKind(java.util.List.of(kind));
		summary.setByDay(java.util.List.of(day));
		ApiResponse<com.wealth.common.model.dto.transaction.TransactionSummaryDto, Void> body = new ApiResponse<>();
		body.setData(summary);
		when(feign.transactionsSummary(any(), any(), any())).thenReturn(ResponseEntity.ok(body));

		com.fasterxml.jackson.databind.JsonNode json = new com.fasterxml.jackson.databind.ObjectMapper().valueToTree(controller.summary("Bearer t", null).getBody());

		assertEquals("investment", json.at("/data/byKind/0/kind").asText(), "the totals by kind reach the screen");
		assertEquals("investment", json.at("/data/byCategory/0/kind").asText(), "each category keeps its kind");
		assertEquals(2, json.at("/data/byCategory/0/trend").size(), "each category keeps its monthly trend");
		assertEquals("2026-11-03", json.at("/data/byDay/0/label").asText(), "the daily totals reach the screen");
	}

	@Test
	void aMissingListRequestMeansTheFirstPageAndBadShapesNeverReachTheBss() {
		when(feign.transactionsList(any(), any(), any())).thenReturn(ResponseEntity.ok(new ApiResponse<>()));
		controller.list("Bearer t", null);
		verify(feign).transactionsList(any(), any(), any());

		TransactionPageRequestDto big = new TransactionPageRequestDto();
		big.setSize(201);
		TransactionPageRequestDto longSearch = new TransactionPageRequestDto();
		longSearch.setQ("x".repeat(101));
		for (TransactionPageRequestDto bad : new TransactionPageRequestDto[] { big, longSearch }) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.list("Bearer t", bad)).getMessage());
		}
		TransactionCreateRequestDto noAmount = new TransactionCreateRequestDto();
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.create("Bearer t", noAmount)).getMessage());
		IdRequestDto badId = new IdRequestDto();
		badId.setId("x");
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.delete("Bearer t", badId)).getMessage());
		verify(feign, never()).transactionsCreate(any(), any(), any());
		verify(feign, never()).transactionsDelete(any(), any(), any());
	}

	@Test
	void theDataQualityListAndADecisionAreForwardedAndABadDecisionNeverReachesTheBss() {
		ApiResponse<java.util.List<com.wealth.common.model.dto.transaction.WorkflowItemDto>, Void> listBody = new ApiResponse<>();
		when(feign.transactionsWorkflowList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(listBody));
		assertEquals(listBody, controller.workflowList("Bearer t").getBody());

		com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto decision = new com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto();
		decision.setId("11111111-2222-3333-4444-555555555555");
		decision.setDecision("merge");
		ApiResponse<Boolean, Void> done = new ApiResponse<>();
		when(feign.transactionsWorkflowDecide("Bearer t", "203.0.113.5", decision)).thenReturn(ResponseEntity.ok(done));
		assertEquals(done, controller.workflowDecide("Bearer t", decision).getBody());

		decision.setDecision("x".repeat(21));
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.workflowDecide("Bearer t", decision)).getMessage());
		decision.setDecision("merge");
		decision.setId("nope");
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.workflowDecide("Bearer t", decision)).getMessage());
	}
}
