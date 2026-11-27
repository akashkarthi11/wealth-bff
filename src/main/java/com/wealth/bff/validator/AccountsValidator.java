package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.AccountIdRequestDto;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.account.AccountCreateRequestDto;
import com.wealth.common.model.dto.account.BalanceRecordRequestDto;
import com.wealth.common.model.dto.account.FdTermsRequestDto;
import com.wealth.bff.constants.WealthConstants;

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

	// Edge check of a new account: a request with a type, an institution and a name of sane length, and a currency of three letters when one is given. The BSS checks the rest.
	public static void validateCreate(AccountCreateRequestDto request) {
		if (request == null || tooLong(request.getSubtype(), WealthConstants.ACCOUNT_SUBTYPE_MAX_LENGTH, true) || tooLong(request.getInstitution(), WealthConstants.ACCOUNT_TEXT_MAX_LENGTH, true)
				|| tooLong(request.getName(), WealthConstants.ACCOUNT_TEXT_MAX_LENGTH, true) || tooLong(request.getMaskedIdentifier(), WealthConstants.ACCOUNT_MASK_MAX_LENGTH, false)
				|| (request.getCurrency() != null && !request.getCurrency().isBlank() && request.getCurrency().trim().length() != WealthConstants.ACCOUNT_CURRENCY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	// Edge check of a balance to record: an account id, an amount, and a status and note of sane length. The BSS checks the account, the time and the rules.
	public static void validateBalance(BalanceRecordRequestDto request) {
		if (request == null || request.getAccountId() == null || !UUID.matcher(request.getAccountId()).matches() || request.getBalance() == null
				|| (request.getStatus() != null && request.getStatus().length() > WealthConstants.BALANCE_STATUS_MAX_LENGTH)
				|| (request.getNote() != null && request.getNote().length() > WealthConstants.BALANCE_NOTE_MAX_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateFdTerms(FdTermsRequestDto request) {
		if (request == null || request.getFdTerms() == null || request.getAccountId() == null || !UUID.matcher(request.getAccountId()).matches()) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static boolean tooLong(String value, int max, boolean required) {
		return value == null ? required : (required && value.isBlank()) || value.trim().length() > max;
	}

	private static void check(String id) {
		if (id == null || !UUID.matcher(id).matches()) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}
}
