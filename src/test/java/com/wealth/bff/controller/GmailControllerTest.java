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
import com.wealth.common.model.dto.admin.GmailAppSaveRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectUrlDto;
import com.wealth.common.model.dto.admin.GmailFetchResultDto;
import com.wealth.common.model.dto.admin.GmailFinishRequestDto;
import com.wealth.common.model.dto.admin.GmailPendingItemDto;
import com.wealth.common.model.dto.admin.GmailReviewRequestDto;
import com.wealth.common.model.dto.admin.GmailStatementResultDto;
import com.wealth.common.model.dto.admin.GmailStatementRowDto;
import com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto;
import com.wealth.common.model.dto.admin.GmailStatusDto;
import com.wealth.common.model.dto.admin.GmailSyncRequestDto;
import com.wealth.common.model.dto.admin.GmailSyncStatusDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class GmailControllerTest {

	private static final String REDIRECT = "https://wealth-ui-ten.vercel.app/my-connections/gmail/callback";
	private static final String EVIL = "http://evil.example.com/x";

	private WealthFeignClient feign;
	private GmailController controller;

	@BeforeEach
	void setUp() {
		feign = mock(WealthFeignClient.class);
		controller = new GmailController(feign);
		RequestHeaderUtil.setIp("203.0.113.5");
	}

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	private GmailConnectRequestDto connect(String uri) {
		GmailConnectRequestDto r = new GmailConnectRequestDto();
		r.setRedirectUri(uri);
		return r;
	}

	private GmailFinishRequestDto finish(String code, String state, String uri) {
		GmailFinishRequestDto r = new GmailFinishRequestDto();
		r.setCode(code);
		r.setState(state);
		r.setRedirectUri(uri);
		return r;
	}

	private GmailAppSaveRequestDto app(String id, String secret) {
		GmailAppSaveRequestDto r = new GmailAppSaveRequestDto();
		r.setClientId(id);
		r.setClientSecret(secret);
		return r;
	}

	@Test
	void statusTestDisconnectAndAppRemoveAreForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<GmailStatusDto, Void> status = new ApiResponse<>();
		ApiResponse<GmailStatusDto, Void> tested = new ApiResponse<>();
		ApiResponse<Boolean, Void> gone = new ApiResponse<>();
		ApiResponse<Boolean, Void> removed = new ApiResponse<>();
		when(feign.gmailStatus("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(status));
		when(feign.gmailTest("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(tested));
		when(feign.gmailDisconnect("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(gone));
		when(feign.gmailAppRemove("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(removed));

		assertEquals(status, controller.status("Bearer t").getBody());
		assertEquals(tested, controller.test("Bearer t").getBody());
		assertEquals(gone, controller.disconnect("Bearer t").getBody());
		assertEquals(removed, controller.appRemove("Bearer t").getBody());
	}

	@Test
	void aConnectUrlIsCheckedAtTheEdgeThenForwarded() {
		GmailConnectRequestDto ok = connect(REDIRECT);
		ApiResponse<GmailConnectUrlDto, Void> body = new ApiResponse<>();
		when(feign.gmailConnectUrl("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.connectUrl("Bearer t", ok).getBody());

		GmailConnectRequestDto local = connect("http://localhost:3000/my-connections/gmail/callback");
		when(feign.gmailConnectUrl("Bearer t", "203.0.113.5", local)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.connectUrl("Bearer t", local).getBody());

		assertEquals(BaseError.GMAIL_REQUEST_INVALID, assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", connect(EVIL))).getMessage());
		assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", connect("javascript:alert(1)")));
		assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", connect("https://a.example.com/x y")));
		assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", connect("https://a.example.com/" + "x".repeat(300))));
		assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", connect(null)));
		assertThrows(KarthiException.class, () -> controller.connectUrl("Bearer t", null));
		verify(feign, never()).gmailConnectUrl(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && EVIL.equals(r.getRedirectUri())));
	}

	@Test
	void aFinishNeedsACodeAndStateOfSaneSizeAndIsForwarded() {
		GmailFinishRequestDto ok = finish("4/abc", "state-token", REDIRECT);
		ApiResponse<GmailStatusDto, Void> body = new ApiResponse<>();
		when(feign.gmailConnectFinish("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.connectFinish("Bearer t", ok).getBody());

		assertEquals(BaseError.GMAIL_REQUEST_INVALID,
				assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", finish("c".repeat(2001), "s", REDIRECT))).getMessage());
		assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", finish("c", "s".repeat(2001), REDIRECT)));
		assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", finish("", "s", REDIRECT)));
		assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", finish("c", null, REDIRECT)));
		assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", finish("c", "s", EVIL)));
		assertThrows(KarthiException.class, () -> controller.connectFinish("Bearer t", null));
		verify(feign, never()).gmailConnectFinish(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && r.getCode() != null && r.getCode().length() > 2000));
	}

	@Test
	void theGoogleClientIsCheckedAtTheEdgeThenForwarded() {
		GmailAppSaveRequestDto ok = app("id.apps.googleusercontent.com", "secret");
		ApiResponse<GmailStatusDto, Void> body = new ApiResponse<>();
		when(feign.gmailAppSave("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.appSave("Bearer t", ok).getBody());

		GmailAppSaveRequestDto keepSecret = app("id.apps.googleusercontent.com", null);
		when(feign.gmailAppSave("Bearer t", "203.0.113.5", keepSecret)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.appSave("Bearer t", keepSecret).getBody());

		GmailAppSaveRequestDto numbersOnly = app(null, null);
		numbersOnly.setStatementDays(365);
		numbersOnly.setStatementMax(60);
		numbersOnly.setAlertDays(60);
		when(feign.gmailAppSave("Bearer t", "203.0.113.5", numbersOnly)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.appSave("Bearer t", numbersOnly).getBody(), "the saved id and secret are kept when only the numbers are sent");
		for (int[] bad : new int[][] { { 6, 1, 7 }, { 731, 1, 7 }, { 30, 0, 7 }, { 30, 101, 7 }, { 30, 5, 6 }, { 30, 5, 731 } }) {
			GmailAppSaveRequestDto outside = app(null, null);
			outside.setStatementDays(bad[0]);
			outside.setStatementMax(bad[1]);
			outside.setAlertDays(bad[2]);
			assertEquals(BaseError.GMAIL_REQUEST_INVALID, assertThrows(KarthiException.class, () -> controller.appSave("Bearer t", outside)).getMessage());
		}
		assertThrows(KarthiException.class, () -> controller.appSave("Bearer t", app("i".repeat(201), "s")));
		assertThrows(KarthiException.class, () -> controller.appSave("Bearer t", app("id", "s".repeat(201))));
		assertThrows(KarthiException.class, () -> controller.appSave("Bearer t", null));
	}

	private GmailReviewRequestDto review(String id, String decision) {
		GmailReviewRequestDto r = new GmailReviewRequestDto();
		r.setId(id);
		r.setDecision(decision);
		return r;
	}

	@Test
	void fetchAndPendingAreForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<GmailFetchResultDto, Void> fetched = new ApiResponse<>();
		ApiResponse<List<GmailPendingItemDto>, Void> pending = new ApiResponse<>();
		when(feign.gmailFetch("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(fetched));
		when(feign.gmailPending("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(pending));

		assertEquals(fetched, controller.fetch("Bearer t").getBody());
		assertEquals(pending, controller.pending("Bearer t").getBody());
	}

	@Test
	void aReviewNeedsASaneIdAndOneOfTheTwoDecisionsThenIsForwarded() {
		GmailReviewRequestDto accept = review("6f1c2d3e-0a1b-4c5d-8e9f-001122334455", "accept");
		GmailReviewRequestDto dismiss = review("abc123", "dismiss");
		ApiResponse<Boolean, Void> body = new ApiResponse<>();
		when(feign.gmailReview("Bearer t", "203.0.113.5", accept)).thenReturn(ResponseEntity.ok(body));
		when(feign.gmailReview("Bearer t", "203.0.113.5", dismiss)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.review("Bearer t", accept).getBody());
		assertEquals(body, controller.review("Bearer t", dismiss).getBody());

		assertEquals(BaseError.GMAIL_REQUEST_INVALID, assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("", "accept"))).getMessage());
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review(null, "accept")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("a".repeat(65), "accept")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("abc; drop", "accept")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("../x", "accept")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("abc", "Accept")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("abc", "delete")));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", review("abc", null)));
		assertThrows(KarthiException.class, () -> controller.review("Bearer t", null));
		verify(feign, never()).gmailReview(any(), any(), org.mockito.ArgumentMatchers.argThat(r -> r != null && "delete".equals(r.getDecision())));
	}

	@Test
	void anExtraStatementPasswordIsCheckedAtTheEdgeThenForwarded() {
		ApiResponse<GmailStatusDto, Void> body = new ApiResponse<>();
		GmailStatementSettingsRequestDto add = passwords(null, null);
		add.setExtraLabel("Slice savings");
		add.setExtraValue("s3cret-Pass");
		when(feign.gmailStatementSave("Bearer t", "203.0.113.5", add)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.statementSave("Bearer t", add).getBody());
		GmailStatementSettingsRequestDto remove = passwords(null, null);
		remove.setRemoveExtra("Slice savings");
		when(feign.gmailStatementSave("Bearer t", "203.0.113.5", remove)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.statementSave("Bearer t", remove).getBody());

		for (String[] bad : new String[][] { { "bad/label", "pw" }, { "x".repeat(31), "pw" }, { "ok", "x".repeat(101) }, { "ok", "a\tb" } }) {
			GmailStatementSettingsRequestDto request = passwords(null, null);
			request.setExtraLabel(bad[0]);
			request.setExtraValue(bad[1]);
			assertEquals(BaseError.GMAIL_REQUEST_INVALID, assertThrows(KarthiException.class, () -> controller.statementSave("Bearer t", request)).getMessage());
		}
		GmailStatementSettingsRequestDto badRemove = passwords(null, null);
		badRemove.setRemoveExtra("no/good");
		assertThrows(KarthiException.class, () -> controller.statementSave("Bearer t", badRemove));
	}

	private GmailStatementSettingsRequestDto passwords(String pan, String dob) {
		GmailStatementSettingsRequestDto r = new GmailStatementSettingsRequestDto();
		r.setPan(pan);
		r.setDob(dob);
		return r;
	}

	@Test
	void statementClearAndStatementsAreForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<GmailStatusDto, Void> cleared = new ApiResponse<>();
		ApiResponse<GmailStatementResultDto, Void> read = new ApiResponse<>();
		when(feign.gmailStatementClear("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(cleared));
		when(feign.gmailStatements("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(read));

		assertEquals(cleared, controller.statementClear("Bearer t").getBody());
		assertEquals(read, controller.statements("Bearer t").getBody());
	}

	@Test
	void theStatementRevealIsForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto, Void> shown = new ApiResponse<>();
		when(feign.gmailStatementReveal("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(shown));
		assertEquals(shown, controller.statementReveal("Bearer t").getBody());
	}

	@Test
	void theStatementHistoryUploadAndRetryAreForwardedAndCheckedAtTheEdge() {
		ApiResponse<List<com.wealth.common.model.dto.admin.StatementHistoryDto>, Void> history = new ApiResponse<>();
		when(feign.gmailStatementHistory("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(history));
		assertEquals(history, controller.statementHistory("Bearer t").getBody());
		ApiResponse<com.wealth.common.model.dto.admin.GmailStatementResultDto, Void> done = new ApiResponse<>();
		when(feign.gmailStatementUpload(any(), any(), any())).thenReturn(ResponseEntity.ok(done));
		when(feign.gmailStatementRetry(any(), any(), any())).thenReturn(ResponseEntity.ok(done));

		com.wealth.common.model.dto.admin.StatementUploadRequestDto upload = new com.wealth.common.model.dto.admin.StatementUploadRequestDto();
		upload.setFileName("a.pdf");
		upload.setData("JVBERi0=");
		controller.statementUpload("Bearer t", upload);
		verify(feign).gmailStatementUpload("Bearer t", "203.0.113.5", upload);
		upload.setFileName("a.txt");
		assertThrows(KarthiException.class, () -> controller.statementUpload("Bearer t", upload));
		upload.setFileName("a.pdf");
		upload.setPassword("p".repeat(101));
		assertThrows(KarthiException.class, () -> controller.statementUpload("Bearer t", upload));

		com.wealth.common.model.dto.admin.StatementRetryRequestDto retry = new com.wealth.common.model.dto.admin.StatementRetryRequestDto();
		retry.setId("11111111-1111-1111-1111-111111111111");
		retry.setPassword("typed");
		controller.statementRetry("Bearer t", retry);
		verify(feign).gmailStatementRetry("Bearer t", "203.0.113.5", retry);
		retry.setId("1; drop");
		assertThrows(KarthiException.class, () -> controller.statementRetry("Bearer t", retry));
		retry.setId("11111111-1111-1111-1111-111111111111");
		retry.setPassword("");
		assertThrows(KarthiException.class, () -> controller.statementRetry("Bearer t", retry));
	}

	@Test
	void theStatementAcceptIsForwardedAndABadIdNeverReachesTheBss() {
		com.wealth.common.model.dto.admin.StatementAcceptRequestDto accept = new com.wealth.common.model.dto.admin.StatementAcceptRequestDto();
		accept.setId("11111111-1111-1111-1111-111111111111");
		ApiResponse<com.wealth.common.model.dto.transaction.BankImportResultDto, Void> body = new ApiResponse<>();
		when(feign.gmailStatementAccept("Bearer t", "203.0.113.5", accept)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.statementAccept("Bearer t", accept).getBody());

		for (String bad : new String[] { "", null, "a".repeat(65), "../x", "a b" }) {
			com.wealth.common.model.dto.admin.StatementAcceptRequestDto request = new com.wealth.common.model.dto.admin.StatementAcceptRequestDto();
			request.setId(bad);
			KarthiException e = assertThrows(KarthiException.class, () -> controller.statementAccept("Bearer t", request));
			assertEquals(BaseError.GMAIL_REQUEST_INVALID, e.getMessage());
		}
		com.wealth.common.model.dto.admin.StatementAcceptRequestDto withAccount = new com.wealth.common.model.dto.admin.StatementAcceptRequestDto();
		withAccount.setId("11111111-1111-1111-1111-111111111111");
		withAccount.setAccountId("33333333-3333-3333-3333-333333333333");
		when(feign.gmailStatementAccept("Bearer t", "203.0.113.5", withAccount)).thenReturn(ResponseEntity.ok(body));
		assertEquals(body, controller.statementAccept("Bearer t", withAccount).getBody(), "an account id is allowed");
		for (String badAccount : new String[] { "../x", "a b", "a".repeat(65) }) {
			com.wealth.common.model.dto.admin.StatementAcceptRequestDto odd = new com.wealth.common.model.dto.admin.StatementAcceptRequestDto();
			odd.setId("11111111-1111-1111-1111-111111111111");
			odd.setAccountId(badAccount);
			assertThrows(KarthiException.class, () -> controller.statementAccept("Bearer t", odd));
		}
		assertThrows(KarthiException.class, () -> controller.statementAccept("Bearer t", null));
		verify(feign, org.mockito.Mockito.times(2)).gmailStatementAccept(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
	}

	@Test
	void theStatementDownloadIsCheckedAtTheEdgeThenForwarded() {
		ApiResponse<com.wealth.common.model.dto.admin.StatementFileDto, Void> file = new ApiResponse<>();
		when(feign.gmailStatementDownload(any(), any(), any())).thenReturn(ResponseEntity.ok(file));
		com.wealth.common.model.dto.admin.StatementDownloadRequestDto request = new com.wealth.common.model.dto.admin.StatementDownloadRequestDto();
		request.setId("11111111-1111-1111-1111-111111111111");
		assertEquals(file, controller.statementDownload("Bearer t", request).getBody());
		verify(feign).gmailStatementDownload("Bearer t", "203.0.113.5", request);
		request.setId("1; drop");
		assertThrows(KarthiException.class, () -> controller.statementDownload("Bearer t", request));
		assertThrows(KarthiException.class, () -> controller.statementDownload("Bearer t", null));
	}

	@Test
	void theStatementListIsForwardedWithTheTokenAndTheCallerIp() {
		ApiResponse<List<GmailStatementRowDto>, Void> rows = new ApiResponse<>();
		when(feign.gmailStatementList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(rows));

		assertEquals(rows, controller.statementList("Bearer t").getBody());
		verify(feign).gmailStatementList("Bearer t", "203.0.113.5");
	}

	@Test
	void aStatementRowCarriesItsFileDate() {
		GmailStatementRowDto row = new GmailStatementRowDto();
		row.setReceivedAt(java.sql.Timestamp.valueOf("2026-11-27 18:28:00"));
		ApiResponse<List<GmailStatementRowDto>, Void> rows = new ApiResponse<>();
		rows.setData(List.of(row));
		when(feign.gmailStatementList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(rows));

		assertEquals(row.getReceivedAt(), controller.statementList("Bearer t").getBody().getData().get(0).getReceivedAt());
	}

	@Test
	void theStatementPasswordsAreCheckedAtTheEdgeThenForwardedAndBlankOnesAreAllowed() {
		ApiResponse<GmailStatusDto, Void> body = new ApiResponse<>();
		GmailStatementSettingsRequestDto both = passwords("ABCDE1234F", "01011990");
		GmailStatementSettingsRequestDto lower = passwords("abcde1234f", null);
		GmailStatementSettingsRequestDto blank = passwords("", "  ");
		GmailStatementSettingsRequestDto nothing = passwords(null, null);
		for (GmailStatementSettingsRequestDto ok : List.of(both, lower, blank, nothing)) {
			when(feign.gmailStatementSave("Bearer t", "203.0.113.5", ok)).thenReturn(ResponseEntity.ok(body));
			assertEquals(body, controller.statementSave("Bearer t", ok).getBody());
		}
		verify(feign, org.mockito.Mockito.times(4)).gmailStatementSave(any(), any(), any());
	}

	@Test
	void aBadPanOrDateOfBirthIsRefusedWithoutTheValueInTheErrorAndNeverForwarded() {
		for (String badPan : new String[] { "ABCDE1234", "ABCDE12345", "ABCD1234FG", "ABCDE1234F ", "ABC-E1234F", "ABCDE1234F; x" }) {
			KarthiException e = assertThrows(KarthiException.class, () -> controller.statementSave("Bearer t", passwords(badPan, "01011990")));
			assertEquals(BaseError.GMAIL_REQUEST_INVALID, e.getMessage());
		}
		for (String badDob : new String[] { "0101199", "010119900", "01-01-1990", "ABCDEFGH", "01011990 " }) {
			KarthiException e = assertThrows(KarthiException.class, () -> controller.statementSave("Bearer t", passwords("ABCDE1234F", badDob)));
			assertEquals(BaseError.GMAIL_REQUEST_INVALID, e.getMessage());
		}
		assertThrows(KarthiException.class, () -> controller.statementSave("Bearer t", null));
		verify(feign, never()).gmailStatementSave(any(), any(), any());
	}

	@Test
	void theBackgroundReadIsStartedAndAskedAboutWithTheTokenAndTheCallerIp() {
		GmailSyncRequestDto request = new GmailSyncRequestDto();
		request.setKind("all");
		ApiResponse<GmailSyncStatusDto, Void> status = new ApiResponse<>();
		when(feign.gmailSyncStart("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(status));
		when(feign.gmailSyncStatus("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(status));

		assertEquals(status, controller.syncStart("Bearer t", request).getBody());
		assertEquals(status, controller.syncStatus("Bearer t").getBody());
	}
}
