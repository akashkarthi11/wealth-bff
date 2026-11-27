package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ApiUsageRequestDto;

// Edge check only: the days asked for are empty (the BSS uses 7) or a whole number from 1 to 30. The BSS checks who may ask.
public final class ApiUsageValidator {

	private ApiUsageValidator() {
	}

	public static void validate(ApiUsageRequestDto request) {
		Integer days = request == null ? null : request.getDays();
		if (days != null && (days < 1 || days > WealthConstants.API_USAGE_DAYS_MAX)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
		String user = request == null || request.getUserId() == null ? "" : request.getUserId().trim();
		if (!user.isEmpty() && !WealthConstants.USAGE_USER_ME.equalsIgnoreCase(user) && user.length() != WealthConstants.USAGE_USER_ID_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}
}
