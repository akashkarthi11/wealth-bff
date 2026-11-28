package com.wealth.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.BillSplitDto;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.model.dto.people.RecordStatusRequestDto;
import com.wealth.common.model.dto.people.RecordStatusResultDto;
import com.wealth.common.model.dto.people.PersonSaveRequestDto;
import com.wealth.common.model.dto.people.PersonSaveResultDto;
import com.wealth.common.model.dto.people.PersonMergeRequestDto;
import com.wealth.common.model.dto.people.PersonMergeResultDto;
import com.wealth.common.model.dto.people.CommentCreateRequestDto;
import com.wealth.common.model.dto.people.CommentCreateResultDto;
import com.wealth.common.model.dto.people.EntryNoteRequestDto;
import com.wealth.common.model.dto.people.EntryNoteResultDto;
import com.wealth.common.model.dto.people.RequestDecideRequestDto;
import com.wealth.common.model.dto.people.RequestDecideResultDto;
import com.wealth.common.model.dto.people.PersonEntryRequestDto;
import com.wealth.common.model.dto.people.ShareUpdateRequestDto;
import com.wealth.common.model.dto.people.ShareUpdateResultDto;
import com.wealth.common.model.dto.people.PersonEntryResultDto;
import com.wealth.common.model.dto.people.RoundOffRequestDto;
import com.wealth.common.model.dto.people.RoundOffResultDto;
import com.wealth.common.model.dto.people.SharePaidRequestDto;
import com.wealth.common.model.dto.people.SharePaidResultDto;
import com.wealth.common.model.dto.people.SplitCreateRequestDto;
import com.wealth.common.model.dto.people.SplitPersonRequestDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

class PeopleControllerTest {

	@AfterEach
	void tearDown() {
		RequestHeaderUtil.clear();
	}

	@Test
	void theListIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<PeopleOverviewDto, Void> body = new ApiResponse<>();
		when(feign.peopleList("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).list("Bearer t").getBody());
	}

	@Test
	void theRefreshButtonIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ApiResponse<PeopleOverviewDto, Void> body = new ApiResponse<>();
		when(feign.peopleRefresh("Bearer t", "203.0.113.5")).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).refresh("Bearer t").getBody());
	}

	private static SplitCreateRequestDto split() {
		SplitCreateRequestDto request = new SplitCreateRequestDto();
		request.setTransactionId("11111111-1111-4111-8111-111111111111");
		request.setMode("equal");
		SplitPersonRequestDto person = new SplitPersonRequestDto();
		person.setName("Arun S");
		request.getPeople().add(person);
		return request;
	}

	@Test
	void aNewSplitIsForwardedWithTheTokenAndTheCallerIp() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		SplitCreateRequestDto request = split();
		ApiResponse<BillSplitDto, Void> body = new ApiResponse<>();
		when(feign.peopleSplitCreate("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));

		assertEquals(body, new PeopleController(feign).createSplit("Bearer t", request).getBody());
	}

	@Test
	void cancellingASplitIsForwardedAndABadIdIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		com.wealth.common.model.dto.IdRequestDto request = new com.wealth.common.model.dto.IdRequestDto();
		request.setId("11111111-1111-4111-8111-111111111111");
		ApiResponse<Boolean, Void> body = new ApiResponse<>();
		when(feign.peopleSplitCancel("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.cancelSplit("Bearer t", request).getBody());
		assertThrows(KarthiException.class, () -> controller.cancelSplit("Bearer t", new com.wealth.common.model.dto.IdRequestDto()));
	}

	@Test
	void aSplitWithoutATransactionOrPeopleOrWithTooManyIsRefusedBeforeTheBss() {
		PeopleController controller = new PeopleController(mock(WealthFeignClient.class));
		SplitCreateRequestDto none = split();
		none.getPeople().clear();
		SplitCreateRequestDto noTransaction = split();
		noTransaction.setTransactionId(null);
		SplitCreateRequestDto many = split();
		for (int i = 0; i < 50; i++) {
			many.getPeople().add(new SplitPersonRequestDto());
		}
		SplitCreateRequestDto longName = split();
		longName.getPeople().get(0).setName("x".repeat(161));

		for (SplitCreateRequestDto bad : new SplitCreateRequestDto[] { none, noTransaction, many, longName, null }) {
			assertEquals(BaseError.INVALID_REQUEST, assertThrows(KarthiException.class, () -> controller.createSplit("Bearer t", bad)).getMessage());
		}
	}
	private static final String ID = "11111111-1111-4111-8111-111111111111";

	@Test
	void newEntriesAreForwardedAndABadIdOrTooManyTransfersAreRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		PersonEntryRequestDto request = new PersonEntryRequestDto();
		request.setName("Naveen K");
		request.getTransactionIds().add(ID);
		ApiResponse<PersonEntryResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleEntryCreate("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.createEntries("Bearer t", request).getBody());

		PersonEntryRequestDto badId = new PersonEntryRequestDto();
		badId.getTransactionIds().add("short");
		assertThrows(KarthiException.class, () -> controller.createEntries("Bearer t", badId));
		PersonEntryRequestDto many = new PersonEntryRequestDto();
		for (int i = 0; i < 501; i++) {
			many.getTransactionIds().add(ID);
		}
		assertThrows(KarthiException.class, () -> controller.createEntries("Bearer t", many));
		PersonEntryRequestDto longName = new PersonEntryRequestDto();
		longName.setName("x".repeat(500));
		assertThrows(KarthiException.class, () -> controller.createEntries("Bearer t", longName));
		assertThrows(KarthiException.class, () -> controller.createEntries("Bearer t", null));
	}

	@Test
	void aPaidShareIsForwardedAndMissingIdsAreRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		SharePaidRequestDto request = new SharePaidRequestDto();
		request.setSplitId(ID);
		request.setPersonId(ID);
		ApiResponse<SharePaidResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleSharePaid("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.markSharePaid("Bearer t", request).getBody());

		SharePaidRequestDto noPerson = new SharePaidRequestDto();
		noPerson.setSplitId(ID);
		assertThrows(KarthiException.class, () -> controller.markSharePaid("Bearer t", noPerson));
		assertThrows(KarthiException.class, () -> controller.markSharePaid("Bearer t", null));
	}

	@Test
	void aRoundOffIsForwardedAndAMissingPersonOrALongDateIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		RoundOffRequestDto request = new RoundOffRequestDto();
		request.setPersonId(ID);
		ApiResponse<RoundOffResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleRoundOff("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.roundOff("Bearer t", request).getBody());

		assertThrows(KarthiException.class, () -> controller.roundOff("Bearer t", new RoundOffRequestDto()));
		RoundOffRequestDto longDate = new RoundOffRequestDto();
		longDate.setPersonId(ID);
		longDate.setClosedOn("2026-11-27T10:00:00Z");
		assertThrows(KarthiException.class, () -> controller.roundOff("Bearer t", longDate));
	}

	@Test
	void aRecordChangeIsForwardedAndAMissingOrLongStatusIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		RecordStatusRequestDto request = new RecordStatusRequestDto();
		request.setEntryId(ID);
		request.setStatus("removed");
		ApiResponse<RecordStatusResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleRecordsUpdate("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.updateRecord("Bearer t", request).getBody());

		assertThrows(KarthiException.class, () -> controller.updateRecord("Bearer t", new RecordStatusRequestDto()));
		RecordStatusRequestDto longStatus = new RecordStatusRequestDto();
		longStatus.setStatus("written_off_for_good");
		assertThrows(KarthiException.class, () -> controller.updateRecord("Bearer t", longStatus));
		assertThrows(KarthiException.class, () -> controller.updateRecord("Bearer t", null));
	}

	@Test
	void aPersonSaveIsForwardedAndLongFieldsAreRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		PersonSaveRequestDto request = new PersonSaveRequestDto();
		request.setPersonId(ID);
		request.setAnnualRate("12");
		request.setDueOn("2026-12-20");
		ApiResponse<PersonSaveResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleSave("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.savePerson("Bearer t", request).getBody());

		PersonSaveRequestDto longRate = new PersonSaveRequestDto();
		longRate.setAnnualRate("1".repeat(40));
		assertThrows(KarthiException.class, () -> controller.savePerson("Bearer t", longRate));
		PersonSaveRequestDto longName = new PersonSaveRequestDto();
		longName.setName("x".repeat(500));
		assertThrows(KarthiException.class, () -> controller.savePerson("Bearer t", longName));
		assertThrows(KarthiException.class, () -> controller.savePerson("Bearer t", null));
	}

	@Test
	void aShareLinkChangeIsForwardedAndAMissingChoiceIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		ShareUpdateRequestDto request = new ShareUpdateRequestDto();
		request.setPersonId(ID);
		request.setEnabled(true);
		ApiResponse<ShareUpdateResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleShareUpdate("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.updateShare("Bearer t", request).getBody());

		ShareUpdateRequestDto noChoice = new ShareUpdateRequestDto();
		noChoice.setPersonId(ID);
		assertThrows(KarthiException.class, () -> controller.updateShare("Bearer t", noChoice));
		assertThrows(KarthiException.class, () -> controller.updateShare("Bearer t", null));
	}

	@Test
	void aMergeIsForwardedAndAMissingPersonIsRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		PersonMergeRequestDto request = new PersonMergeRequestDto();
		request.setFromId(ID);
		request.setIntoId("22222222-2222-4222-8222-222222222222");
		ApiResponse<PersonMergeResultDto, Void> body = new ApiResponse<>();
		when(feign.peopleMerge("Bearer t", "203.0.113.5", request)).thenReturn(ResponseEntity.ok(body));
		PeopleController controller = new PeopleController(feign);

		assertEquals(body, controller.mergePeople("Bearer t", request).getBody());

		PersonMergeRequestDto noTarget = new PersonMergeRequestDto();
		noTarget.setFromId(ID);
		assertThrows(KarthiException.class, () -> controller.mergePeople("Bearer t", noTarget));
		assertThrows(KarthiException.class, () -> controller.mergePeople("Bearer t", null));
	}

	@Test
	void anAnswerANoteAndADecisionAreForwardedAndBadOnesAreRefusedBeforeTheBss() {
		WealthFeignClient feign = mock(WealthFeignClient.class);
		RequestHeaderUtil.setIp("203.0.113.5");
		PeopleController controller = new PeopleController(feign);
		CommentCreateRequestDto comment = new CommentCreateRequestDto();
		comment.setEntryId(ID);
		comment.setBody("thanks");
		ApiResponse<CommentCreateResultDto, Void> commentBody = new ApiResponse<>();
		when(feign.peopleCommentCreate("Bearer t", "203.0.113.5", comment)).thenReturn(ResponseEntity.ok(commentBody));
		assertEquals(commentBody, controller.createComment("Bearer t", comment).getBody());
		CommentCreateRequestDto longBody = new CommentCreateRequestDto();
		longBody.setBody("x".repeat(700));
		assertThrows(KarthiException.class, () -> controller.createComment("Bearer t", longBody));

		EntryNoteRequestDto note = new EntryNoteRequestDto();
		note.setEntryId(ID);
		note.setNote("for the cab");
		ApiResponse<EntryNoteResultDto, Void> noteBody = new ApiResponse<>();
		when(feign.peopleEntryNote("Bearer t", "203.0.113.5", note)).thenReturn(ResponseEntity.ok(noteBody));
		assertEquals(noteBody, controller.updateEntryNote("Bearer t", note).getBody());
		assertThrows(KarthiException.class, () -> controller.updateEntryNote("Bearer t", new EntryNoteRequestDto()));

		RequestDecideRequestDto decide = new RequestDecideRequestDto();
		decide.setRequestId(ID);
		decide.setDecision("accept");
		ApiResponse<RequestDecideResultDto, Void> decideBody = new ApiResponse<>();
		when(feign.peopleRequestDecide("Bearer t", "203.0.113.5", decide)).thenReturn(ResponseEntity.ok(decideBody));
		assertEquals(decideBody, controller.decideRequest("Bearer t", decide).getBody());
		RequestDecideRequestDto longDecision = new RequestDecideRequestDto();
		longDecision.setRequestId(ID);
		longDecision.setDecision("accept-everything");
		assertThrows(KarthiException.class, () -> controller.decideRequest("Bearer t", longDecision));
		assertThrows(KarthiException.class, () -> controller.decideRequest("Bearer t", null));
	}
}
