package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfGmailContract;
import com.wealth.bff.validator.GmailValidator;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.admin.GmailAppSaveRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectUrlDto;
import com.wealth.common.model.dto.admin.GmailFetchResultDto;
import com.wealth.common.model.dto.admin.GmailSyncRequestDto;
import com.wealth.common.model.dto.admin.GmailSyncStatusDto;
import com.wealth.common.model.dto.admin.GmailFinishRequestDto;
import com.wealth.common.model.dto.admin.GmailPendingItemDto;
import com.wealth.common.model.dto.admin.GmailReviewRequestDto;
import com.wealth.common.model.dto.admin.GmailStatementResultDto;
import com.wealth.common.model.dto.admin.GmailStatementRowDto;
import com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto;
import com.wealth.common.model.dto.admin.StatementHistoryDto;
import com.wealth.common.model.dto.admin.StatementDownloadRequestDto;
import com.wealth.common.model.dto.admin.StatementFileDto;
import com.wealth.common.model.dto.admin.StatementRetryRequestDto;
import com.wealth.common.model.dto.admin.StatementUploadRequestDto;
import com.wealth.common.model.dto.admin.GmailStatusDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class GmailController extends WealthBaseController implements IfGmailContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(GmailController.class);

	private final WealthFeignClient feignClient;

	public GmailController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> status(String authorization) throws KarthiException {
		LOGGER.info("Entering status BFF");
		return handleResponse(feignClient.gmailStatus(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailConnectUrlDto, Void>> connectUrl(String authorization, GmailConnectRequestDto request) throws KarthiException {
		LOGGER.info("Entering connectUrl BFF");
		GmailValidator.validateConnect(request);
		return handleResponse(feignClient.gmailConnectUrl(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> connectFinish(String authorization, GmailFinishRequestDto request) throws KarthiException {
		LOGGER.info("Entering connectFinish BFF");
		GmailValidator.validateFinish(request);
		return handleResponse(feignClient.gmailConnectFinish(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> test(String authorization) throws KarthiException {
		LOGGER.info("Entering test BFF");
		return handleResponse(feignClient.gmailTest(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> disconnect(String authorization) throws KarthiException {
		LOGGER.info("Entering disconnect BFF");
		return handleResponse(feignClient.gmailDisconnect(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> appSave(String authorization, GmailAppSaveRequestDto request) throws KarthiException {
		LOGGER.info("Entering appSave BFF");
		GmailValidator.validateApp(request);
		return handleResponse(feignClient.gmailAppSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> appRemove(String authorization) throws KarthiException {
		LOGGER.info("Entering appRemove BFF");
		return handleResponse(feignClient.gmailAppRemove(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailFetchResultDto, Void>> fetch(String authorization) throws KarthiException {
		LOGGER.info("Entering fetch BFF");
		return handleResponse(feignClient.gmailFetch(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailSyncStatusDto, Void>> syncStart(String authorization, GmailSyncRequestDto request) throws KarthiException {
		LOGGER.info("Entering syncStart BFF");
		return handleResponse(feignClient.gmailSyncStart(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailSyncStatusDto, Void>> syncStatus(String authorization) throws KarthiException {
		LOGGER.info("Entering syncStatus BFF");
		return handleResponse(feignClient.gmailSyncStatus(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<List<GmailPendingItemDto>, Void>> pending(String authorization) throws KarthiException {
		LOGGER.info("Entering pending BFF");
		return handleResponse(feignClient.gmailPending(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<Boolean, Void>> review(String authorization, GmailReviewRequestDto request) throws KarthiException {
		LOGGER.info("Entering review BFF");
		GmailValidator.validateReview(request);
		return handleResponse(feignClient.gmailReview(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> statementSave(String authorization, GmailStatementSettingsRequestDto request) throws KarthiException {
		LOGGER.info("Entering statementSave BFF");
		GmailValidator.validateStatementSettings(request);
		return handleResponse(feignClient.gmailStatementSave(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto, Void>> statementReveal(String authorization) throws KarthiException {
		LOGGER.info("Entering statementReveal BFF");
		return handleResponse(feignClient.gmailStatementReveal(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatusDto, Void>> statementClear(String authorization) throws KarthiException {
		LOGGER.info("Entering statementClear BFF");
		return handleResponse(feignClient.gmailStatementClear(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statements(String authorization) throws KarthiException {
		LOGGER.info("Entering statements BFF");
		return handleResponse(feignClient.gmailStatements(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<List<StatementHistoryDto>, Void>> statementHistory(String authorization) throws KarthiException {
		LOGGER.info("Entering statementHistory BFF");
		return handleResponse(feignClient.gmailStatementHistory(authorization, RequestHeaderUtil.getIp()));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statementUpload(String authorization, StatementUploadRequestDto request) throws KarthiException {
		LOGGER.info("Entering statementUpload BFF");
		GmailValidator.validateStatementUpload(request);
		return handleResponse(feignClient.gmailStatementUpload(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<StatementFileDto, Void>> statementDownload(String authorization, StatementDownloadRequestDto request) throws KarthiException {
		LOGGER.info("Entering statementDownload BFF");
		GmailValidator.validateStatementDownload(request);
		return handleResponse(feignClient.gmailStatementDownload(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statementRetry(String authorization, StatementRetryRequestDto request) throws KarthiException {
		LOGGER.info("Entering statementRetry BFF");
		GmailValidator.validateStatementRetry(request);
		return handleResponse(feignClient.gmailStatementRetry(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.BankImportResultDto, Void>> statementAccept(String authorization,
			com.wealth.common.model.dto.admin.StatementAcceptRequestDto request) throws KarthiException {
		LOGGER.info("Entering statementAccept BFF");
		GmailValidator.validateStatementAccept(request);
		return handleResponse(feignClient.gmailStatementAccept(authorization, RequestHeaderUtil.getIp(), request));
	}

	@Override
	public ResponseEntity<ApiResponse<List<GmailStatementRowDto>, Void>> statementList(String authorization) throws KarthiException {
		LOGGER.info("Entering statementList BFF");
		return handleResponse(feignClient.gmailStatementList(authorization, RequestHeaderUtil.getIp()));
	}
}
