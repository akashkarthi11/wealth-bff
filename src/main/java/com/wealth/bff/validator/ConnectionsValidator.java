package com.wealth.bff.validator;

import java.net.URI;
import java.util.Locale;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

// Shape of a service address: http or https, a host, an optional port, nothing else. Returns it cleaned as scheme://host[:port].
public final class ConnectionsValidator {

	private static final String LINK_LOCAL_PREFIX = "169.254.";

	private ConnectionsValidator() {
	}

	public static String validateUrl(ConnectionRequestDto request) {
		String text = request == null || request.getUrl() == null ? "" : request.getUrl().trim();
		if (text.isEmpty() || text.length() > WealthConstants.CONNECTION_URL_MAX_LENGTH) {
			throw new KarthiException(BaseError.CONNECTION_ADDRESS_INVALID);
		}
		try {
			URI uri = URI.create(text);
			String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
			String path = uri.getRawPath() == null ? "" : uri.getRawPath();
			boolean plainOrigin = (path.isEmpty() || path.equals("/")) && uri.getRawQuery() == null && uri.getRawFragment() == null
					&& uri.getRawUserInfo() == null;
			if (!(scheme.equals("http") || scheme.equals("https")) || uri.getHost() == null || !plainOrigin || uri.getHost().startsWith(LINK_LOCAL_PREFIX)) {
				throw new KarthiException(BaseError.CONNECTION_ADDRESS_INVALID);
			}
			return scheme + "://" + uri.getHost().toLowerCase(Locale.ROOT) + (uri.getPort() == -1 ? "" : ":" + uri.getPort());
		} catch (IllegalArgumentException e) {
			throw new KarthiException(BaseError.CONNECTION_ADDRESS_INVALID);
		}
	}
}
