package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.invest.HoldingSaveRequestDto;

// Edge checks only: an account id, a name, an asset class and a unit of sane length, a quantity and, when given, a price. The BSS checks the account, the rules and the dates.
public final class InvestmentsValidator {

	private static final Pattern UUID = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	private InvestmentsValidator() {
	}

	public static void validateHolding(HoldingSaveRequestDto request) {
		if (request == null || request.getAccountId() == null || !UUID.matcher(request.getAccountId()).matches() || blankOrLong(request.getName(), WealthConstants.HOLDING_TEXT_MAX_LENGTH)
				|| blankOrLong(request.getAssetClass(), WealthConstants.HOLDING_CLASS_MAX_LENGTH) || blankOrLong(request.getUnit(), WealthConstants.HOLDING_UNIT_MAX_LENGTH)
				|| (request.getSymbol() != null && request.getSymbol().length() > WealthConstants.HOLDING_TEXT_MAX_LENGTH) || request.getQuantity() == null) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static boolean blankOrLong(String value, int max) {
		return value == null || value.isBlank() || value.trim().length() > max;
	}
}
