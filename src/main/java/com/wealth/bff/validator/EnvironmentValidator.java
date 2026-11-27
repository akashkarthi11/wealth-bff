package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

// Edge check only: the text looks like host:port (a Kafka list may have commas and spaces) and the username and password are not too long.
// The BSS checks the real rules and that the address answers.
public final class EnvironmentValidator {

	private static final Pattern SHAPE = Pattern.compile("^[A-Za-z0-9.:, -]+$");
	private static final Pattern SERVICE_ADDRESS = Pattern.compile("^https?://[A-Za-z0-9.:-]+/?$");

	private EnvironmentValidator() {
	}

	public static void validateAddress(ConnectionRequestDto request) {
		String text = request == null ? null : request.getUrl();
		if (text == null || text.isBlank() || text.length() > WealthConstants.ENVIRONMENT_ADDRESS_MAX_LENGTH || !SHAPE.matcher(text).matches()) {
			throw new KarthiException(BaseError.ENVIRONMENT_ADDRESS_INVALID);
		}
		if (tooLong(request.getUsername(), WealthConstants.CREDENTIAL_USERNAME_MAX) || tooLong(request.getPassword(), WealthConstants.CREDENTIAL_PASSWORD_MAX)) {
			throw new KarthiException(BaseError.ENVIRONMENT_CREDENTIALS_INVALID);
		}
	}

	// The Active / Inactive switch (Redis and Kafka): the request must say true or false. The BSS decides who may switch.
	public static void validateActive(ConnectionRequestDto request) {
		if (request == null || request.getEnabled() == null) {
			throw new KarthiException(BaseError.PROVIDER_INVALID);
		}
	}

	// A service address (http://host:port): the key is one of the two known ones and the text has only URL characters. The BSS checks the rest.
	// Edge check only: a number must be there. The BSS decides the allowed range.
	public static void validateRefreshTime(ConnectionRequestDto request) {
		if (request == null || request.getRefreshSeconds() == null) {
			throw new KarthiException(BaseError.REFRESH_TIME_INVALID);
		}
	}

	public static void validateServiceAddress(ConnectionRequestDto request) {
		String key = request == null ? null : request.getKey();
		String url = request == null ? null : request.getUrl();
		boolean knownKey = WealthConstants.SETTING_UI_TO_BFF.equals(key) || WealthConstants.SETTING_BFF_TO_BSS.equals(key);
		if (!knownKey || url == null || url.isBlank() || url.length() > WealthConstants.CONNECTION_URL_MAX_LENGTH || !SERVICE_ADDRESS.matcher(url).matches()) {
			throw new KarthiException(BaseError.CONNECTION_ADDRESS_INVALID);
		}
	}

	private static boolean tooLong(String text, int max) {
		return text != null && text.length() > max;
	}
}
