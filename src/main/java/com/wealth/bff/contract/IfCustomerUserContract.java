package com.wealth.bff.contract;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.LoginResponseDto;
import com.wealth.common.model.dto.customer.PasswordChangeRequestDto;
import com.wealth.common.model.dto.customer.ProfileSaveRequestDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;
import com.wealth.common.model.dto.customer.UserDto;

@RequestMapping(WealthConstants.USERS_BASE)
public interface IfCustomerUserContract {

	// Public: no token yet.
	@PostMapping(WealthConstants.LOGIN)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> login(@RequestBody LoginRequestDto request)
			throws KarthiException;

	// Public: guarded by the secret key, checked in the BSS.
	@PostMapping(WealthConstants.REGISTER)
	ResponseEntity<ApiResponse<LoginResponseDto, Void>> register(@RequestBody RegisterRequestDto request)
			throws KarthiException;

	@PostMapping(WealthConstants.ME)
	ResponseEntity<ApiResponse<UserDto, Void>> me(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;

	@PostMapping(WealthConstants.LOGOUT)
	ResponseEntity<ApiResponse<Boolean, Void>> logout(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization) throws KarthiException;

	// The signed-in user's own profile details.
	@PostMapping(WealthConstants.PROFILE_SAVE)
	ResponseEntity<ApiResponse<UserDto, Void>> profileSave(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody ProfileSaveRequestDto request) throws KarthiException;

	// The signed-in user changes their own password; every older session of theirs ends.
	@PostMapping(WealthConstants.PASSWORD_CHANGE)
	ResponseEntity<ApiResponse<Boolean, Void>> changePassword(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody PasswordChangeRequestDto request) throws KarthiException;

	// Admin only, decided in the BSS.
	@PostMapping(WealthConstants.PASSWORD_RESET)
	ResponseEntity<ApiResponse<Boolean, Void>> resetPassword(
			@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization,
			@RequestBody PasswordResetRequestDto request) throws KarthiException;
}
