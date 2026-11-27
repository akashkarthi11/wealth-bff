package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.BrokerUserSaveRequestDto;

// Edge checks only: a login value is at most 200 characters with no control character (a blank one keeps the saved value). The values are never put in the error.
public final class BrokerValidator {

	private BrokerValidator() {
	}

	public static void validateSave(BrokerUserSaveRequestDto request) {
		if (request == null || !fine(request.getApiKey()) || !fine(request.getClientCode()) || !fine(request.getPin()) || !fine(request.getTotpSecret()) || !fine(request.getMacAddress())) {
			throw new KarthiException(BaseError.PROVIDER_INVALID);
		}
	}

	private static boolean fine(String value) {
		return value == null || (value.length() <= WealthConstants.BROKER_VALUE_EDGE_MAX && value.chars().noneMatch(c -> c < ' ' || c == 127));
	}
}
