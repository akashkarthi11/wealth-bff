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
import com.wealth.common.model.dto.account.AccountDetailDto;
import com.wealth.common.model.dto.account.AccountSplitDto;
import com.wealth.common.model.dto.account.AccountSubtypeDto;
import com.wealth.common.model.dto.account.AccountsOverviewDto;
import com.wealth.common.model.dto.admin.ConnectionDto;
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartyDto;
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

	// Service to service: no user token, the shared service key instead.
	@PostMapping(WealthConstants.BSS_INTERNAL_SETTINGS_BASE + WealthConstants.INTERNAL_PICK)
	ResponseEntity<ApiResponse<ConnectionDto, Void>> internalSettingsPick(
			@RequestHeader(value = WealthConstants.HEADER_SERVICE_KEY, required = false) String serviceKey, @RequestBody ConnectionRequestDto request);

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

	@PostMapping(WealthConstants.BSS_THIRD_PARTY_BASE + WealthConstants.THIRD_PARTY_REMOVE)
	ResponseEntity<ApiResponse<Boolean, Void>> thirdPartyRemove(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip, @RequestBody ProviderRequestDto request);

	@PostMapping(WealthConstants.BSS_MENUS_BASE + WealthConstants.MENUS_LIST)
	ResponseEntity<ApiResponse<List<NavMenuDto>, Void>> menusList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

	@PostMapping(WealthConstants.BSS_ACCOUNTS_BASE + WealthConstants.ACCOUNTS_LIST)
	ResponseEntity<ApiResponse<AccountsOverviewDto, Void>> accountsList(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestHeader(value = WealthConstants.HEADER_FORWARDED_FOR, required = false) String ip);

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
}
