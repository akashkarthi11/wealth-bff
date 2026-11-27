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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.account.AccountDetailDto;
import com.wealth.common.model.dto.account.AccountSplitDto;
import com.wealth.common.model.dto.account.AccountSubtypeDto;
import com.wealth.common.model.dto.account.AccountsOverviewDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class AccountsControllerTest {

	private static final String ID = "2a7014a8-9cf0-4d39-bed2-c83a83eddf0c";

	private WealthFeignClient feign;
	private AccountsController controller;

	@org.junit.jupiter.api.Test
	void theRefreshButtonIsForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<AccountsOverviewDto, Void> body = new ApiResponse<>();
		when(feign.accountsRefresh("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.refresh("Bearer t").getBody());
	}

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new AccountsController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private IdRequestDto id(String value) {
		IdRequestDto r = new IdRequestDto();
		r.setId(value);
		return r;
	}

	private AccountIdRequestDto accountId(String value) {
		AccountIdRequestDto r = new AccountIdRequestDto();
		r.setAccountId(value);
		return r;
	}

	@Test
	void listForwardsTheTokenAndTheCallerIpAndReturnsTheBssAnswerAsIs() {
		ApiResponse<AccountsOverviewDto, Void> body = new ApiResponse<>();
		when(feign.accountsList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> out = controller.list("Bearer t");

		assertEquals(HttpStatus.OK, out.getStatusCode());
		assertEquals(body, out.getBody());
	}

	@Test
	void subtypesForwardsTheToken() {
		ApiResponse<List<AccountSubtypeDto>, Void> body = new ApiResponse<>();
		when(feign.accountsSubtypes("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.subtypes("Bearer t").getBody());
	}

	@Test
	void detailChecksTheIdAtTheEdgeThenForwards() {
		IdRequestDto ok = id(ID);
		ApiResponse<AccountDetailDto, Void> body = new ApiResponse<>();
		when(feign.accountsDetail("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.detail("Bearer t", ok).getBody());
		assertEquals(BaseError.INVALID_REQUEST,
				assertThrows(KarthiException.class, () -> controller.detail("Bearer t", id("1; drop table"))).getMessage());
		verify(feign, never()).accountsDetail(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> "1; drop table".equals(r.getId())));
	}

	@Test
	void splitsListChecksTheAccountIdAtTheEdgeThenForwards() {
		AccountIdRequestDto ok = accountId(ID);
		ApiResponse<List<AccountSplitDto>, Void> body = new ApiResponse<>();
		when(feign.accountsSplitsList("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.splitsList("Bearer t", ok).getBody());
		assertThrows(KarthiException.class, () -> controller.splitsList("Bearer t", accountId(null)));
		assertThrows(KarthiException.class, () -> controller.splitsList("Bearer t", null));
	}

	private com.wealth.common.model.dto.account.AccountCreateRequestDto newAccount(String subtype, String institution, String name) {
		com.wealth.common.model.dto.account.AccountCreateRequestDto r = new com.wealth.common.model.dto.account.AccountCreateRequestDto();
		r.setSubtype(subtype);
		r.setInstitution(institution);
		r.setName(name);
		return r;
	}

	@Test
	void anAccountToAddIsForwardedWithTheTokenAndABadShapeNeverReachesTheBss() {
		ApiResponse<com.wealth.common.model.dto.account.AccountDto, Void> body = new ApiResponse<>();
		com.wealth.common.model.dto.account.AccountCreateRequestDto good = newAccount("savings", "HDFC", "Main");
		when(feign.accountsCreate("Bearer t", "203.0.113.5", good)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, controller.create("Bearer t", good).getBody());

		com.wealth.common.model.dto.account.AccountCreateRequestDto currency = newAccount("savings", "HDFC", "Main");
		currency.setCurrency("RUPEE");
		for (com.wealth.common.model.dto.account.AccountCreateRequestDto bad : java.util.Arrays.asList(null, newAccount(null, "HDFC", "Main"), newAccount("savings", " ", "Main"),
				newAccount("savings", "HDFC", "x".repeat(81)), currency)) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.create("Bearer t", bad)).getMessage());
		}
		verify(feign, never()).accountsCreate("Bearer t", "203.0.113.5", currency);
	}

	@Test
	void aBalanceAndFdTermsAreForwardedWithTheTokenAndABadShapeNeverReachesTheBss() {
		com.wealth.common.model.dto.account.BalanceRecordRequestDto balance = new com.wealth.common.model.dto.account.BalanceRecordRequestDto();
		balance.setAccountId("11111111-2222-3333-4444-555555555555");
		balance.setBalance(new java.math.BigDecimal("10"));
		ApiResponse<com.wealth.common.model.dto.account.BalanceRecordResultDto, Void> stored = new ApiResponse<>();
		when(feign.accountsRecordBalance("Bearer t", "203.0.113.5", balance)).thenReturn(ResponseEntity.ok(stored));
		assertEquals(stored, controller.recordBalance("Bearer t", balance).getBody());

		com.wealth.common.model.dto.account.BalanceRecordRequestDto noAmount = new com.wealth.common.model.dto.account.BalanceRecordRequestDto();
		noAmount.setAccountId(balance.getAccountId());
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.recordBalance("Bearer t", noAmount)).getMessage());
		balance.setAccountId("nope");
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.recordBalance("Bearer t", balance)).getMessage());

		com.wealth.common.model.dto.account.FdTermsRequestDto terms = new com.wealth.common.model.dto.account.FdTermsRequestDto();
		terms.setAccountId("11111111-2222-3333-4444-555555555555");
		terms.setFdTerms(new com.wealth.common.model.dto.account.FdTermsDto());
		ApiResponse<com.wealth.common.model.dto.account.AccountDto, Void> updated = new ApiResponse<>();
		when(feign.accountsUpdateFdTerms("Bearer t", "203.0.113.5", terms)).thenReturn(ResponseEntity.ok(updated));
		assertEquals(updated, controller.updateFdTerms("Bearer t", terms).getBody());
		terms.setFdTerms(null);
		assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.updateFdTerms("Bearer t", terms)).getMessage());
	}
}
