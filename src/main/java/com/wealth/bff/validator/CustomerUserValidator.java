package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;

// Edge checks only; the password policy and the secret key are decided in the BSS.
public final class CustomerUserValidator {

	private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

	private CustomerUserValidator() {
	}

	public static void validateLogin(LoginRequestDto request) {
		requireBody(request);
		validateEmail(request.getEmail());
		validatePassword(request.getPassword());
	}

	public static void validateRegister(RegisterRequestDto request) {
		requireBody(request);
		validateEmail(request.getEmail());
		validatePassword(request.getPassword());
	}

	public static void validatePasswordReset(PasswordResetRequestDto request) {
		requireBody(request);
		validateEmail(request.getEmail());
		validatePassword(request.getNewPassword());
	}

	private static void requireBody(Object request) {
		if (request == null) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static void validateEmail(String raw) {
		String email = raw == null ? "" : raw.trim();
		if (email.isEmpty()) {
			throw new KarthiException(BaseError.EMAIL_CANNOT_BE_NULL_OR_EMPTY);
		}
		if (email.length() > WealthConstants.EMAIL_MAX_LENGTH || !EMAIL.matcher(email).matches()) {
			throw new KarthiException(BaseError.INVALID_EMAIL_FORMAT);
		}
	}

	private static void validatePassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new KarthiException(BaseError.PASSWORD_EMPTY);
		}
		if (password.length() > WealthConstants.PASSWORD_MAX_LENGTH) {
			throw new KarthiException(BaseError.PASSWORD_TOO_LONG);
		}
	}
}
