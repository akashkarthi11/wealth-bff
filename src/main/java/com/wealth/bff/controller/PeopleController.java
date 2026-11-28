package com.wealth.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfPeopleContract;
import com.wealth.bff.validator.AccountsValidator;
import com.wealth.bff.validator.PeopleValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
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
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class PeopleController extends WealthBaseController implements IfPeopleContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(PeopleController.class);

	private final WealthFeignClient feignClient;

	public PeopleController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.peopleList(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> refresh(String authorization) throws KarthiException {
		LOGGER.info("Entering refresh BFF");
		return handleResponse(feignClient.peopleRefresh(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<BillSplitDto, Void>> createSplit(String authorization, SplitCreateRequestDto request) throws KarthiException {
		LOGGER.info("Entering createSplit BFF");
		PeopleValidator.validateSplit(request);
		return handleResponse(feignClient.peopleSplitCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> cancelSplit(String authorization, IdRequestDto request) throws KarthiException {
		LOGGER.info("Entering cancelSplit BFF");
		AccountsValidator.validateId(request);
		return handleResponse(feignClient.peopleSplitCancel(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<PersonEntryResultDto, Void>> createEntries(String authorization, PersonEntryRequestDto request) throws KarthiException {
		LOGGER.info("Entering createEntries BFF");
		PeopleValidator.validateEntry(request);
		return handleResponse(feignClient.peopleEntryCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<SharePaidResultDto, Void>> markSharePaid(String authorization, SharePaidRequestDto request) throws KarthiException {
		LOGGER.info("Entering markSharePaid BFF");
		PeopleValidator.validateSharePaid(request);
		return handleResponse(feignClient.peopleSharePaid(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<RoundOffResultDto, Void>> roundOff(String authorization, RoundOffRequestDto request) throws KarthiException {
		LOGGER.info("Entering roundOff BFF");
		PeopleValidator.validateRoundOff(request);
		return handleResponse(feignClient.peopleRoundOff(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<RecordStatusResultDto, Void>> updateRecord(String authorization, RecordStatusRequestDto request) throws KarthiException {
		LOGGER.info("Entering updateRecord BFF");
		PeopleValidator.validateRecord(request);
		return handleResponse(feignClient.peopleRecordsUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<PersonSaveResultDto, Void>> savePerson(String authorization, PersonSaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering savePerson BFF");
		PeopleValidator.validateSave(request);
		return handleResponse(feignClient.peopleSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<ShareUpdateResultDto, Void>> updateShare(String authorization, ShareUpdateRequestDto request) throws KarthiException {
		LOGGER.info("Entering updateShare BFF");
		PeopleValidator.validateShare(request);
		return handleResponse(feignClient.peopleShareUpdate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<PersonMergeResultDto, Void>> mergePeople(String authorization, PersonMergeRequestDto request) throws KarthiException {
		LOGGER.info("Entering mergePeople BFF");
		PeopleValidator.validateMerge(request);
		return handleResponse(feignClient.peopleMerge(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<CommentCreateResultDto, Void>> createComment(String authorization, CommentCreateRequestDto request) throws KarthiException {
		LOGGER.info("Entering createComment BFF");
		PeopleValidator.validateComment(request);
		return handleResponse(feignClient.peopleCommentCreate(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<EntryNoteResultDto, Void>> updateEntryNote(String authorization, EntryNoteRequestDto request) throws KarthiException {
		LOGGER.info("Entering updateEntryNote BFF");
		PeopleValidator.validateNote(request);
		return handleResponse(feignClient.peopleEntryNote(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<RequestDecideResultDto, Void>> decideRequest(String authorization, RequestDecideRequestDto request) throws KarthiException {
		LOGGER.info("Entering decideRequest BFF");
		PeopleValidator.validateDecide(request);
		return handleResponse(feignClient.peopleRequestDecide(authorization, RequestHeaderUtil.getIp(), request));
	}
}
