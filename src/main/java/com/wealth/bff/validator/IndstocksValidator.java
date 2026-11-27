package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.IndstocksSaveRequestDto;

// Edge checks only: the token is present, at most 500 characters, with no control character or space. The value is never put in the error.
public final class IndstocksValidator {

	private IndstocksValidator() {
	}

	public static void validateSave(IndstocksSaveRequestDto request) {
		String token = request == null ? null : request.getToken();
		if (token == null || token.isBlank() || token.length() > WealthConstants.INDSTOCKS_TOKEN_EDGE_MAX || token.trim().chars().anyMatch(c -> c < ' ' || c == 127)) {
			throw new KarthiException(BaseError.PROVIDER_INVALID);
		}
	}
}
