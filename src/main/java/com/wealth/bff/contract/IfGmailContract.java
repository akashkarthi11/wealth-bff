package com.wealth.bff.contract;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
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

// Gmail connection: each signed-in user connects their own mailbox; app-save and app-remove (the Google client) are admin only, decided in the BSS.
@RequestMapping(WealthConstants.GMAIL_BASE)
public interface IfGmailContract {

	@PostMapping(WealthConstants.GMAIL_STATUS)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> status(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_CONNECT_URL)
	ResponseEntity<ApiResponse<GmailConnectUrlDto, Void>> connectUrl(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailConnectRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_CONNECT_FINISH)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> connectFinish(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailFinishRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_TEST)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> test(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_DISCONNECT)
	ResponseEntity<ApiResponse<Boolean, Void>> disconnect(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_APP_SAVE)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> appSave(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailAppSaveRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_APP_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> appRemove(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_FETCH)
	ResponseEntity<ApiResponse<GmailFetchResultDto, Void>> fetch(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_SYNC_START)
	ResponseEntity<ApiResponse<GmailSyncStatusDto, Void>> syncStart(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailSyncRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_SYNC_STATUS)
	ResponseEntity<ApiResponse<GmailSyncStatusDto, Void>> syncStatus(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_PENDING)
	ResponseEntity<ApiResponse<List<GmailPendingItemDto>, Void>> pending(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_REVIEW)
	ResponseEntity<ApiResponse<Boolean, Void>> review(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailReviewRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_SAVE)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> statementSave(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody GmailStatementSettingsRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_REVEAL)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto, Void>> statementReveal(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_CLEAR)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> statementClear(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENTS)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statements(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_HISTORY)
	ResponseEntity<ApiResponse<List<StatementHistoryDto>, Void>> statementHistory(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_UPLOAD)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statementUpload(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody StatementUploadRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_DOWNLOAD)
	ResponseEntity<ApiResponse<StatementFileDto, Void>> statementDownload(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody StatementDownloadRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_ROWS)
	ResponseEntity<ApiResponse<List<com.wealth.common.model.dto.admin.StatementRowDto>, Void>> statementRows(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody StatementDownloadRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_RETRY)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> statementRetry(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody StatementRetryRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_ACCEPT)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.BankImportResultDto, Void>> statementAccept(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody com.wealth.common.model.dto.admin.StatementAcceptRequestDto request) throws KarthiException;

	@PostMapping(WealthConstants.GMAIL_STATEMENT_LIST)
	ResponseEntity<ApiResponse<List<GmailStatementRowDto>, Void>> statementList(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;
}
