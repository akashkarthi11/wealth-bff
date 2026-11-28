package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.IdRequestDto;
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

// Signed-in users; the call needs the token and the BSS returns only that user's people.
@RequestMapping(WealthConstants.PEOPLE_BASE)
public interface IfPeopleContract {

	@PostMapping(WealthConstants.PEOPLE_LIST)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_REFRESH)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> refresh(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SPLIT_CANCEL)
	ResponseEntity<ApiResponse<Boolean, Void>> cancelSplit(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody IdRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SPLIT_CREATE)
	ResponseEntity<ApiResponse<BillSplitDto, Void>> createSplit(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody SplitCreateRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_ENTRY_CREATE)
	ResponseEntity<ApiResponse<PersonEntryResultDto, Void>> createEntries(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody PersonEntryRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SHARE_PAID)
	ResponseEntity<ApiResponse<SharePaidResultDto, Void>> markSharePaid(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody SharePaidRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_ROUNDOFF)
	ResponseEntity<ApiResponse<RoundOffResultDto, Void>> roundOff(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody RoundOffRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_RECORDS_UPDATE)
	ResponseEntity<ApiResponse<RecordStatusResultDto, Void>> updateRecord(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody RecordStatusRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SAVE)
	ResponseEntity<ApiResponse<PersonSaveResultDto, Void>> savePerson(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody PersonSaveRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_SHARE_UPDATE)
	ResponseEntity<ApiResponse<ShareUpdateResultDto, Void>> updateShare(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody ShareUpdateRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_MERGE)
	ResponseEntity<ApiResponse<PersonMergeResultDto, Void>> mergePeople(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody PersonMergeRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_COMMENT_CREATE)
	ResponseEntity<ApiResponse<CommentCreateResultDto, Void>> createComment(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody CommentCreateRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_ENTRY_NOTE)
	ResponseEntity<ApiResponse<EntryNoteResultDto, Void>> updateEntryNote(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody EntryNoteRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.PEOPLE_REQUEST_DECIDE)
	ResponseEntity<ApiResponse<RequestDecideResultDto, Void>> decideRequest(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization, @RequestBody RequestDecideRequestDto request)
			throws KarthiException;
}
