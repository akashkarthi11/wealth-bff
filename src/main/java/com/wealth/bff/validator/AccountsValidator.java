package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;

// Edge checks only: the id has the shape of an account id. Whether it is the caller's account is decided in the BSS.
public final class AccountsValidator {

	private static final Pattern UUID = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	private AccountsValidator() {
	}

	public static void validateId(IdRequestDto request) {
		check(request == null ? null : request.getId());
	}

	public static void validateAccountId(AccountIdRequestDto request) {
		check(request == null ? null : request.getAccountId());
	}

	private static void check(String id) {
		if (id == null || !UUID.matcher(id).matches()) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}
}
