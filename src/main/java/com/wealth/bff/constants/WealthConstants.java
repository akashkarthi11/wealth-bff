package com.wealth.bff.constants;

public final class WealthConstants {

	private WealthConstants() {
	}

	public static final int EMAIL_MAX_LENGTH = 254;
	public static final int PASSWORD_MAX_LENGTH = 200;

	// Frontend-facing paths of the BFF.
	public static final String API_BASE = "/wealth/api/v1";
	public static final String USERS_BASE = API_BASE + "/customers/users";
	public static final String HEALTH_BASE = API_BASE + "/health";

	// Paths of the BSS, called only through WealthFeignClient.
	public static final String BSS_USERS_BASE = "/wealth-service/api/v1/customers/users";
	public static final String BSS_ACTUATOR_HEALTH = "/actuator/health";

	// Action paths, the same on the BFF and the BSS.
	public static final String LOGIN = "/login";
	public static final String REGISTER = "/register";
	public static final String ME = "/me";
	public static final String LOGOUT = "/logout";
	public static final String PASSWORD_RESET = "/password/reset";

	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";

	// Health report keys and values.
	public static final String KEY_BFF = "bff";
	public static final String KEY_BSS = "bss";
	public static final String KEY_DB = "db";
	public static final String KEY_REDIS = "redis";
	public static final String KEY_STATUS = "status";
	public static final String KEY_COMPONENTS = "components";
	public static final String STATUS_UP = "UP";
	public static final String STATUS_DOWN = "DOWN";
	public static final String STATUS_UNKNOWN = "UNKNOWN";
}
