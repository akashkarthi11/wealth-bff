package com.wealth.bff.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// The BSS address the BFF calls. The deployed one is the default; an admin can point it elsewhere. Kept in memory only, so a restart returns to the default.
@Component
public class BssEndpoint {

	private final String defaultUrl;
	private volatile String override;

	public BssEndpoint(@Value("${wealth.bss.base-url}") String baseUrl) {
		this.defaultUrl = trim(baseUrl);
	}

	public String defaultUrl() {
		return defaultUrl;
	}

	public String current() {
		String chosen = override;
		return chosen == null ? defaultUrl : chosen;
	}

	public boolean isCustomised() {
		return override != null;
	}

	public void set(String url) {
		String clean = trim(url);
		this.override = clean.equals(defaultUrl) ? null : clean;
	}

	public void reset() {
		this.override = null;
	}

	private static String trim(String url) {
		return url == null ? "" : url.replaceAll("/+$", "");
	}
}
