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

	// Admin module: BFF paths, the BSS paths they call, and the action paths (same on both).
	public static final String ADMIN_BASE = API_BASE + "/admin";
	public static final String BSS_ADMIN_BASE = "/wealth-service/api/v1/admin";
	public static final String ROLES_LIST = "/roles/list";
	public static final String ROLES_UPDATE_MENUS = "/roles/update-menus";
	public static final String RECORDS_LIST = "/records/list";
	public static final String THIRD_PARTY_BASE = ADMIN_BASE + "/connections";
	public static final String BSS_THIRD_PARTY_BASE = "/wealth-service/api/v1/admin/connections";
	public static final String THIRD_PARTY_LIST = "/list";
	public static final String THIRD_PARTY_SAVE = "/save";
	public static final String THIRD_PARTY_TEST = "/test";
	public static final String THIRD_PARTY_REMOVE = "/remove";
	public static final String THIRD_PARTY_FETCH = "/fetch";
	public static final int EXTRA_EDGE_MAX_ENTRIES = 10;
	public static final int EXTRA_EDGE_MAX_LENGTH = 200;
	public static final int PROVIDER_EDGE_MAX = 60;
	public static final int PROVIDER_URL_EDGE_MAX = 200;
	public static final int PROVIDER_KEY_EDGE_MAX = 500;
	public static final String ROLES_CREATE = "/roles/create";
	public static final String USERS_LIST = "/users/list";
	public static final String USERS_SET_ROLE = "/users/set-role";
	public static final int ROLE_LABEL_EDGE_MAX = 60;

	public static final String ENVIRONMENT_BASE = ADMIN_BASE + "/environment";
	public static final String BSS_ENVIRONMENT_BASE = "/wealth-service/api/v1/admin/environment";
	public static final String ENV_REDIS_GET = "/redis/get";
	public static final String ENV_REDIS_UPDATE = "/redis/update";
	public static final String ENV_REDIS_RESET = "/redis/reset";
	public static final String ENV_KAFKA_GET = "/kafka/get";
	public static final String ENV_KAFKA_UPDATE = "/kafka/update";
	public static final String ENV_KAFKA_RESET = "/kafka/reset";
	public static final String ENV_POSTGRES_GET = "/postgres/get";
	public static final String ENV_POSTGRES_UPDATE = "/postgres/update";
	public static final String ENV_POSTGRES_RESET = "/postgres/reset";
	public static final String ENV_ADDRESS_SAVE = "/address/save";
	public static final String MENUS_BASE = API_BASE + "/menus";
	public static final String BSS_MENUS_BASE = "/wealth-service/api/v1/menus";
	public static final String MENUS_LIST = "/list";
	public static final String INTERNAL_SETTINGS_BASE = API_BASE + "/internal/settings";
	public static final String BSS_INTERNAL_SETTINGS_BASE = "/wealth-service/api/v1/internal/settings";
	public static final String INTERNAL_PICK = "/pick";
	public static final String HEADER_SERVICE_KEY = "X-Service-Key";
	public static final String SETTING_UI_TO_BFF = "ui_to_bff";
	public static final String SETTING_BFF_TO_BSS = "bff_to_bss";
	public static final int ENVIRONMENT_ADDRESS_MAX_LENGTH = 300;
	public static final int CREDENTIAL_USERNAME_MAX = 100;
	public static final int CREDENTIAL_PASSWORD_MAX = 200;

	public static final String CONNECTIONS_BASE = ADMIN_BASE + "/connections";
	public static final String CONNECTION_BSS_GET = "/bss/get";
	public static final String CONNECTION_BSS_UPDATE = "/bss/update";
	public static final String CONNECTION_BSS_RESET = "/bss/reset";
	public static final int CONNECTION_URL_MAX_LENGTH = 200;
	public static final int CONNECTION_PROBE_TIMEOUT_SECONDS = 5;
	public static final String ROLE_ADMIN = "admin";

	public static final String ACCOUNTS_BASE = API_BASE + "/accounts";
	public static final String BSS_ACCOUNTS_BASE = "/wealth-service/api/v1/accounts";
	public static final String ACCOUNTS_LIST = "/list";
	public static final String ACCOUNTS_DETAIL = "/detail";
	public static final String ACCOUNTS_SUBTYPES = "/subtypes";
	public static final String ACCOUNTS_SPLITS_LIST = "/splits/list";

	// Edge limits for the admin requests.
	public static final int MENU_KEY_MAX_LENGTH = 30;
	public static final int MENUS_MAX = 50;
	public static final int ROLE_MAX_LENGTH = 30;
	public static final int SEARCH_MAX_LENGTH = 100;

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
