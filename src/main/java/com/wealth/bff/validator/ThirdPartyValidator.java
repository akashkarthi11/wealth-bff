package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ProviderRequestDto;
import com.wealth.common.model.dto.admin.ThirdPartySaveRequestDto;

// Edge checks only: shapes and lengths. The BSS decides what is a valid provider, address and key, and who may change them.
public final class ThirdPartyValidator {

	private static final Pattern PROVIDER = Pattern.compile("^[a-z0-9-]{1,40}$");

	private ThirdPartyValidator() {
	}

	public static void validateSave(ThirdPartySaveRequestDto request) {
		if (request == null || request.getProvider() == null || !PROVIDER.matcher(request.getProvider()).matches() || tooLong(request.getLabel(), WealthConstants.PROVIDER_EDGE_MAX)
				|| tooLong(request.getBaseUrl(), WealthConstants.PROVIDER_URL_EDGE_MAX)) {
			throw new KarthiException(BaseError.PROVIDER_INVALID);
		}
		if (tooLong(request.getApiKey(), WealthConstants.PROVIDER_KEY_EDGE_MAX)) {
			throw new KarthiException(BaseError.ENVIRONMENT_CREDENTIALS_INVALID);
		}
		if (request.getExtra() != null && (request.getExtra().size() > WealthConstants.EXTRA_EDGE_MAX_ENTRIES
				|| request.getExtra().values().stream().anyMatch(v -> tooLong(v, WealthConstants.EXTRA_EDGE_MAX_LENGTH)))) {
			throw new KarthiException(BaseError.ENVIRONMENT_CREDENTIALS_INVALID);
		}
	}

	public static void validateProvider(ProviderRequestDto request) {
		if (request == null || request.getProvider() == null || !PROVIDER.matcher(request.getProvider()).matches()) {
			throw new KarthiException(BaseError.PROVIDER_INVALID);
		}
	}

	private static boolean tooLong(String text, int max) {
		return text != null && text.length() > max;
	}
}
