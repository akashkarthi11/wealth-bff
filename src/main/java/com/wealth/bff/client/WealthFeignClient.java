package com.wealth.bff.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.account.AccountCreateRequestDto;
import com.wealth.common.model.dto.account.BalanceRecordRequestDto;
import com.wealth.common.model.dto.account.BalanceRecordResultDto;
import com.wealth.common.model.dto.account.FdTermsRequestDto;
import com.wealth.common.model.dto.account.AccountDetailDto;
import com.wealth.common.model.dto.account.AccountDto;
import com.wealth.common.model.dto.invest.HoldingDto;
import com.wealth.common.model.dto.invest.HoldingSaveRequestDto;
import com.wealth.common.model.dto.invest.InvestmentsDto;
import com.wealth.common.model.dto.people.PeopleOverviewDto;
import com.wealth.common.model.dto.transaction.TransactionCreateRequestDto;
import com.wealth.common.model.dto.transaction.TransactionDto;
import com.wealth.common.model.dto.transaction.TransactionPageDto;
import com.wealth.common.model.dto.transaction.TransactionPageRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowItemDto;
import com.wealth.common.model.dto.account.AccountSplitDto;
import com.wealth.common.model.dto.account.AccountSubtypeDto;
import com.wealth.common.model.dto.account.AccountsOverviewDto;
import com.wealth.common.model.dto.admin.ApiUsageDto;
import com.wealth.common.model.dto.admin.ApiUsageRequestDto;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.GmailAppSaveRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectUrlDto;
import com.wealth.common.model.dto.admin.GmailFetchResultDto;
import com.wealth.common.model.dto.admin.GmailFinishRequestDto;
import com.wealth.common.model.dto.admin.GmailPendingItemDto;
import com.wealth.common.model.dto.admin.GmailReviewRequestDto;
import com.wealth.common.model.dto.admin.BrokerUserSaveRequestDto;
import com.wealth.common.model.dto.admin.BrokerUserStatusDto;
import com.wealth.common.model.dto.admin.IndstocksSaveRequestDto;
import com.wealth.common.model.dto.admin.IndstocksStatusDto;
import com.wealth.common.model.dto.admin.GmailStatementResultDto;
import com.wealth.common.model.dto.admin.StatementHistoryDto;
import com.wealth.common.model.dto.admin.StatementDownloadRequestDto;
import com.wealth.common.model.dto.admin.StatementFileDto;
import com.wealth.common.model.dto.admin.StatementRetryRequestDto;
import com.wealth.common.model.dto.admin.StatementUploadRequestDto;
import com.wealth.common.model.dto.admin.GmailStatementRowDto;
import com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto;
import com.wealth.common.model.dto.admin.GmailStatusDto;
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartyDto;
import com.wealth.common.model.dto.admin.UsageBatchDto;
import com.wealth.common.model.dto.admin.ThirdPartySaveRequestDto;
import com.wealth.common.model.dto.admin.RoleCreateRequestDto;
import com.wealth.common.model.dto.admin.RoleDto;
import com.wealth.common.model.dto.admin.UserAccessDto;
import com.wealth.common.model.dto.admin.UserRoleRequestDto;
import com.wealth.common.model.dto.menu.NavMenuDto;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;
import com.wealth.common.model.dto.admin.RecordsRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
import com.wealth.common.model.dto.admin.RolesOverviewDto;
import com.wealth.common.model.dto.admin.SnapshotDto;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordChangeRequestDto;
import com.wealth.common.model.dto.customer.ProfileSaveRequestDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;
import com.wealth.common.model.dto.customer.UserDto;

// The single Feign client of the BFF; every module adds its calls here. The caller IP goes through as X-Forwarded-For.
@FeignClient(name = "wealth-bss", url = "${wealth.bss.base-url}")
public interface WealthFeignClient {

	@GetMapping(WealthConstants.BSS_ACTUATOR_HEALTH)
	Map<String, Object> health();

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.LOGIN)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> login(
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody LoginRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.REGISTER)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> register(
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody RegisterRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.ME)
	ResponseEntity<ApiResponse<UserDto, Void>> me(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.LOGOUT)
	ResponseEntity<ApiResponse<Boolean, Void>> logout(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.PROFILE_SAVE)
	ResponseEntity<ApiResponse<UserDto, Void>> profileSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody ProfileSaveRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.PASSWORD_CHANGE)
	ResponseEntity<ApiResponse<Boolean, Void>> changePassword(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody PasswordChangeRequestDto request);

	@PostMapping(WealthConstants.BSS_USERS_BASE + WealthConstants.PASSWORD_RESET)
	ResponseEntity<ApiResponse<Boolean, Void>> resetPassword(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody PasswordResetRequestDto request);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.ROLES_LIST)
	ResponseEntity<ApiResponse<RolesOverviewDto, Void>> adminRolesList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.ROLES_UPDATE_MENUS)
	ResponseEntity<ApiResponse<RoleMenusDto, Void>> adminUpdateRoleMenus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody RoleMenusDto request);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.RECORDS_LIST)
	ResponseEntity<ApiResponse<List<SnapshotDto>, Void>> adminRecordsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody(required = false) RecordsRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_REDIS_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentRedisGet(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_REDIS_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentRedisUpdate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_REDIS_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentRedisReset(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_REDIS_ACTIVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentRedisActive(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_KAFKA_ACTIVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentKafkaActive(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_KAFKA_REFRESH)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentKafkaRefreshTime(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_KAFKA_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentKafkaGet(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_KAFKA_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentKafkaUpdate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_KAFKA_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentKafkaReset(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_POSTGRES_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentPostgresGet(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_POSTGRES_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentPostgresUpdate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_POSTGRES_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentPostgresReset(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_ADDRESS_SAVE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentAddressSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_SIGNUP_GET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentSignupGet(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_SIGNUP_UPDATE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentSignupUpdate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ConnectionRequestDto request);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_SIGNUP_CLOSE)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentSignupClose(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ENVIRONMENT_BASE + WealthConstants.ENV_SIGNUP_RESET)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> environmentSignupReset(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	// Service to service: no user token, the shared service key instead.
	@PostMapping(WealthConstants.BSS_INTERNAL_SETTINGS_BASE + WealthConstants.INTERNAL_PICK)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> internalSettingsPick(
			@RequestHeader(value = WealthConstants.HEADER_SERVICE_KEY, required = false) String serviceKey, @RequestBody ConnectionRequestDto request);

	// Service to service: the BFF reports its API usage counts to the BSS (shared service key, no user token).
	@PostMapping(WealthConstants.BSS_INTERNAL_USAGE_BASE + WealthConstants.INTERNAL_USAGE_RECORD)
	ResponseEntity<ApiResponse<Integer, Void>> internalUsageRecord(
			@RequestHeader(value = WealthConstants.HEADER_SERVICE_KEY, required = false) String serviceKey, @RequestBody UsageBatchDto batch);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.ROLES_CREATE)
	ResponseEntity<ApiResponse<RoleDto, Void>> adminCreateRole(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody RoleCreateRequestDto request);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.USERS_LIST)
	ResponseEntity<ApiResponse<List<UserAccessDto>, Void>> adminUsersList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ADMIN_BASE + WealthConstants.USERS_SET_ROLE)
	ResponseEntity<ApiResponse<UserAccessDto, Void>> adminSetUserRole(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody UserRoleRequestDto request);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_LIST)
	ResponseEntity<ApiResponse<List<ThirdPartyDto>, Void>> thirdPartyList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_SAVE)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> thirdPartySave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ThirdPartySaveRequestDto request);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_TEST)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> thirdPartyTest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ProviderRequestDto request);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_FETCH)
	ResponseEntity<ApiResponse<ThirdPartyDto, Void>> thirdPartyFetch(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ProviderRequestDto request);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_OVERVIEW)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.admin.ConnectionsOverviewDto, Void>> thirdPartyOverview(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> thirdPartyRemove(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ProviderRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATUS)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailStatus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_CONNECT_URL)
	ResponseEntity<ApiResponse<GmailConnectUrlDto, Void>> gmailConnectUrl(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody GmailConnectRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_CONNECT_FINISH)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailConnectFinish(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody GmailFinishRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_TEST)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailTest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_DISCONNECT)
	ResponseEntity<ApiResponse<Boolean, Void>> gmailDisconnect(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_APP_SAVE)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailAppSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody GmailAppSaveRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_APP_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> gmailAppRemove(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_FETCH)
	ResponseEntity<ApiResponse<GmailFetchResultDto, Void>> gmailFetch(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_PENDING)
	ResponseEntity<ApiResponse<List<GmailPendingItemDto>, Void>> gmailPending(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_REVIEW)
	ResponseEntity<ApiResponse<Boolean, Void>> gmailReview(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody GmailReviewRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_SAVE)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailStatementSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody GmailStatementSettingsRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_REVEAL)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto, Void>> gmailStatementReveal(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_CLEAR)
	ResponseEntity<ApiResponse<GmailStatusDto, Void>> gmailStatementClear(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENTS)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> gmailStatements(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_LIST)
	ResponseEntity<ApiResponse<List<GmailStatementRowDto>, Void>> gmailStatementList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_HISTORY)
	ResponseEntity<ApiResponse<List<StatementHistoryDto>, Void>> gmailStatementHistory(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_UPLOAD)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> gmailStatementUpload(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody StatementUploadRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_RETRY)
	ResponseEntity<ApiResponse<GmailStatementResultDto, Void>> gmailStatementRetry(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody StatementRetryRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_ACCEPT)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.BankImportResultDto, Void>> gmailStatementAccept(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody com.wealth.common.model.dto.admin.StatementAcceptRequestDto request);

	@PostMapping(WealthConstants.BSS_GMAIL_BASE + WealthConstants.GMAIL_STATEMENT_DOWNLOAD)
	ResponseEntity<ApiResponse<StatementFileDto, Void>> gmailStatementDownload(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody StatementDownloadRequestDto request);

	@PostMapping(WealthConstants.BSS_BROKER_BASE + WealthConstants.BROKER_STATUS)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> brokerStatus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_BROKER_BASE + WealthConstants.BROKER_SAVE)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> brokerSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody BrokerUserSaveRequestDto request);

	@PostMapping(WealthConstants.BSS_BROKER_BASE + WealthConstants.BROKER_TEST)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> brokerTest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_BROKER_BASE + WealthConstants.BROKER_FETCH)
	ResponseEntity<ApiResponse<BrokerUserStatusDto, Void>> brokerFetch(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_BROKER_BASE + WealthConstants.BROKER_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> brokerRemove(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_INDSTOCKS_BASE + WealthConstants.BROKER_STATUS)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> indstocksStatus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_INDSTOCKS_BASE + WealthConstants.BROKER_SAVE)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> indstocksSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody IndstocksSaveRequestDto request);

	@PostMapping(WealthConstants.BSS_INDSTOCKS_BASE + WealthConstants.BROKER_TEST)
	ResponseEntity<ApiResponse<IndstocksStatusDto, Void>> indstocksTest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_INDSTOCKS_BASE + WealthConstants.BROKER_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> indstocksRemove(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_API_USAGE_BASE + WealthConstants.API_USAGE_LIST)
	ResponseEntity<ApiResponse<ApiUsageDto, Void>> apiUsageList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody(required = false) ApiUsageRequestDto request);

	@PostMapping(WealthConstants.BSS_MENUS_BASE + WealthConstants.MENUS_LIST)
	ResponseEntity<ApiResponse<List<NavMenuDto>, Void>> menusList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_LIST)
	ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> accountsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_INVESTMENTS_BASE + WealthConstants.INVESTMENTS_REFRESH)
	ResponseEntity<ApiResponse<InvestmentsDto, Void>> investmentsRefresh(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_REFRESH)
	ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> accountsRefresh(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_INVESTMENTS_BASE + WealthConstants.INVESTMENTS_HOLDING_SAVE)
	ResponseEntity<ApiResponse<HoldingDto, Void>> investmentsSaveHolding(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody HoldingSaveRequestDto request);

	@PostMapping(WealthConstants.BSS_INVESTMENTS_BASE + WealthConstants.INVESTMENTS_LIST)
	ResponseEntity<ApiResponse<InvestmentsDto, Void>> investmentsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_PEOPLE_BASE + WealthConstants.PEOPLE_LIST)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> peopleList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_PEOPLE_BASE + WealthConstants.PEOPLE_REFRESH)
	ResponseEntity<ApiResponse<PeopleOverviewDto, Void>> peopleRefresh(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_PEOPLE_BASE + WealthConstants.PEOPLE_SPLIT_CREATE)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.people.BillSplitDto, Void>> peopleSplitCreate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody com.wealth.common.model.dto.people.SplitCreateRequestDto request);

	@PostMapping(WealthConstants.BSS_PEOPLE_BASE + WealthConstants.PEOPLE_SPLIT_CANCEL)
	ResponseEntity<ApiResponse<Boolean, Void>> peopleSplitCancel(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody com.wealth.common.model.dto.IdRequestDto request);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_CREATE)
	ResponseEntity<ApiResponse<AccountDto, Void>> accountsCreate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody AccountCreateRequestDto request);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_BALANCE_RECORD)
	ResponseEntity<ApiResponse<BalanceRecordResultDto, Void>> accountsRecordBalance(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody BalanceRecordRequestDto request);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_FD_TERMS_UPDATE)
	ResponseEntity<ApiResponse<AccountDto, Void>> accountsUpdateFdTerms(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody FdTermsRequestDto request);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_LIST)
	ResponseEntity<ApiResponse<TransactionPageDto, Void>> transactionsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody TransactionPageRequestDto request);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_SUMMARY)
	ResponseEntity<ApiResponse<com.wealth.common.model.dto.transaction.TransactionSummaryDto, Void>> transactionsSummary(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody TransactionPageRequestDto request);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_REFRESH)
	ResponseEntity<ApiResponse<TransactionPageDto, Void>> transactionsRefresh(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_CREATE)
	ResponseEntity<ApiResponse<TransactionDto, Void>> transactionsCreate(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody TransactionCreateRequestDto request);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_WORKFLOW_LIST)
	ResponseEntity<ApiResponse<List<WorkflowItemDto>, Void>> transactionsWorkflowList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_WORKFLOW_DECIDE)
	ResponseEntity<ApiResponse<Boolean, Void>> transactionsWorkflowDecide(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody WorkflowDecisionRequestDto request);

	@PostMapping(WealthConstants.BSS_TRANSACTIONS_BASE + WealthConstants.TRANSACTIONS_DELETE)
	ResponseEntity<ApiResponse<Boolean, Void>> transactionsDelete(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody IdRequestDto request);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_DETAIL)
	ResponseEntity<ApiResponse<AccountDetailDto, Void>> accountsDetail(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody IdRequestDto request);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_SUBTYPES)
	ResponseEntity<ApiResponse<List<AccountSubtypeDto>, Void>> accountsSubtypes(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_SPLITS_LIST)
	ResponseEntity<ApiResponse<List<AccountSplitDto>, Void>> accountsSplitsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody AccountIdRequestDto request);
	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_CONFIG)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuConfiguration(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_CONSENT_REQUEST)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuConsentRequest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody Map<String, Object> request);

	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_CONSENT_STATUS)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuConsentStatus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody Map<String, Object> request);

	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_FI_REQUEST)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuFiRequest(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody Map<String, Object> request);

	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_FI_STATUS)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuFiStatus(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody Map<String, Object> request);

	@PostMapping(WealthConstants.BSS_FINVU_AA_BASE + WealthConstants.FINVU_AA_FI_FETCH)
	ResponseEntity<ApiResponse<Map<String, Object>, Void>> finvuFiFetch(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip,
			@RequestBody Map<String, Object> request);

}