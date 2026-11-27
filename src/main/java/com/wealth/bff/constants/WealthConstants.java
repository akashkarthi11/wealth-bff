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
	public static final String THIRD_PARTY_OVERVIEW = "/overview";
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
	public static final String ENV_REDIS_ACTIVE = "/redis/active";
	public static final String ENV_KAFKA_GET = "/kafka/get";
	public static final String ENV_KAFKA_UPDATE = "/kafka/update";
	public static final String ENV_KAFKA_RESET = "/kafka/reset";
	public static final String ENV_KAFKA_ACTIVE = "/kafka/active";
	public static final String ENV_KAFKA_REFRESH = "/kafka/refresh-time";
	public static final String ENV_POSTGRES_GET = "/postgres/get";
	public static final String ENV_POSTGRES_UPDATE = "/postgres/update";
	public static final String ENV_POSTGRES_RESET = "/postgres/reset";
	public static final String ENV_ADDRESS_SAVE = "/address/save";
	public static final String MENUS_BASE = API_BASE + "/menus";
	public static final String BSS_MENUS_BASE = "/wealth-service/api/v1/menus";
	public static final String MENUS_LIST = "/list";
	public static final String API_USAGE_BASE = API_BASE + "/advisor/api-usage";
	public static final String BSS_API_USAGE_BASE = "/wealth-service/api/v1/advisor/api-usage";
	public static final String API_USAGE_LIST = "/list";
	public static final int API_USAGE_DAYS_MAX = 30;
	public static final String BSS_INTERNAL_USAGE_BASE = "/wealth-service/api/v1/internal/usage";
	public static final String INTERNAL_USAGE_RECORD = "/record";
	// The BFF reports its API usage to the BSS every 15 seconds: at most 500 names per report (the BSS refuses more), 2,000 names held while the BSS is away.
	public static final long USAGE_REPORT_MILLIS = 15_000;
	public static final int USAGE_REPORT_NAMES_MAX = 500;
	public static final int USAGE_HELD_NAMES_MAX = 2_000;
	public static final int USAGE_NAME_MAX = 200;
	public static final String USAGE_UNMATCHED = "UNMATCHED";
	public static final String USAGE_USER_ME = "me";
	public static final int USAGE_USER_ID_LENGTH = 36;
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
	public static final String ACCOUNTS_REFRESH = "/refresh";
	public static final String ACCOUNTS_CREATE = "/create";
	public static final String ACCOUNTS_BALANCE_RECORD = "/balances/record";
	public static final String ACCOUNTS_FD_TERMS_UPDATE = "/fd-terms/update";
	public static final int BALANCE_STATUS_MAX_LENGTH = 30;
	public static final int BALANCE_NOTE_MAX_LENGTH = 300;
	public static final int ACCOUNT_TEXT_MAX_LENGTH = 80;
	public static final int ACCOUNT_MASK_MAX_LENGTH = 20;
	public static final int ACCOUNT_SUBTYPE_MAX_LENGTH = 40;
	public static final int ACCOUNT_CURRENCY_LENGTH = 3;
	public static final String ACCOUNTS_DETAIL = "/detail";
	public static final String ACCOUNTS_SUBTYPES = "/subtypes";
	public static final String ACCOUNTS_SPLITS_LIST = "/splits/list";
	public static final String INVESTMENTS_BASE = API_BASE + "/investments";
	public static final String BSS_INVESTMENTS_BASE = "/wealth-service/api/v1/investments";
	public static final String INVESTMENTS_LIST = "/list";
	public static final String INVESTMENTS_REFRESH = "/refresh";
	public static final String TRANSACTIONS_BASE = API_BASE + "/transactions";
	public static final String BSS_TRANSACTIONS_BASE = "/wealth-service/api/v1/transactions";
	public static final String TRANSACTIONS_LIST = "/list";
	public static final String TRANSACTIONS_REFRESH = "/refresh";
	public static final String TRANSACTIONS_SUMMARY = "/summary";
	public static final String TRANSACTIONS_CREATE = "/create";
	public static final String TRANSACTIONS_DELETE = "/delete";
	public static final String TRANSACTIONS_WORKFLOW_LIST = "/workflow/list";
	public static final String TRANSACTIONS_WORKFLOW_DECIDE = "/workflow/decide";
	public static final int WORKFLOW_DECISION_MAX_LENGTH = 20;
	public static final int TRANSACTION_PAGE_MAX = 200;
	public static final int TRANSACTION_SEARCH_MAX_LENGTH = 100;
	public static final int TRANSACTION_CURSOR_MAX_LENGTH = 300;
	public static final int TRANSACTION_KIND_MAX_LENGTH = 20;
	public static final int TRANSACTION_CATEGORY_MAX_LENGTH = 60;
	public static final int TRANSACTION_DESCRIPTION_MAX_LENGTH = 180;
	public static final int TRANSACTION_REFERENCE_MAX_LENGTH = 120;
	public static final int TRANSACTION_DAY_LENGTH = 10;
	public static final String INVESTMENTS_HOLDING_SAVE = "/holdings/save";
	public static final int HOLDING_TEXT_MAX_LENGTH = 80;
	public static final int HOLDING_CLASS_MAX_LENGTH = 30;
	public static final int HOLDING_UNIT_MAX_LENGTH = 10;
	public static final String PEOPLE_BASE = API_BASE + "/people";
	public static final String BSS_PEOPLE_BASE = "/wealth-service/api/v1/people";
	public static final String PEOPLE_LIST = "/list";
	public static final String PEOPLE_REFRESH = "/refresh";

	// Gmail connection (each user's own mailbox; the Google client is set by an admin). Same action paths on the BFF and the BSS.
	public static final String BROKER_BASE = API_BASE + "/broker";
	public static final String BSS_BROKER_BASE = "/wealth-service/api/v1/broker";
	public static final String BROKER_STATUS = "/status";
	public static final String BROKER_SAVE = "/save";
	public static final String BROKER_TEST = "/test";
	public static final String BROKER_FETCH = "/fetch";
	public static final String BROKER_REMOVE = "/remove";
	public static final int BROKER_VALUE_EDGE_MAX = 200;
	public static final String GMAIL_BASE = API_BASE + "/gmail";
	public static final String BSS_GMAIL_BASE = "/wealth-service/api/v1/gmail";
	public static final String GMAIL_STATUS = "/status";
	public static final String GMAIL_CONNECT_URL = "/connect-url";
	public static final String GMAIL_CONNECT_FINISH = "/connect-finish";
	public static final String GMAIL_TEST = "/test";
	public static final String GMAIL_DISCONNECT = "/disconnect";
	public static final String GMAIL_APP_SAVE = "/app-save";
	public static final String GMAIL_APP_REMOVE = "/app-remove";
	public static final String GMAIL_FETCH = "/fetch";
	public static final String GMAIL_PENDING = "/pending";
	public static final String GMAIL_REVIEW = "/review";
	public static final String GMAIL_STATEMENT_SAVE = "/statement-save";
	public static final String GMAIL_STATEMENT_CLEAR = "/statement-clear";
	public static final String GMAIL_STATEMENT_REVEAL = "/statement-reveal";
	public static final String GMAIL_STATEMENTS = "/statements";
	public static final String GMAIL_STATEMENT_LIST = "/statement-list";
	public static final String GMAIL_STATEMENT_HISTORY = "/statement-history";
	public static final String GMAIL_STATEMENT_UPLOAD = "/statement-upload";
	public static final String GMAIL_STATEMENT_RETRY = "/statement-retry";
	public static final String GMAIL_STATEMENT_DOWNLOAD = "/statement-download";
	public static final String GMAIL_STATEMENT_ACCEPT = "/statement-accept";
	public static final int GMAIL_UPLOAD_DATA_EDGE_MAX = 7_000_000;
	public static final int GMAIL_FILE_NAME_EDGE_MAX = 200;
	public static final int GMAIL_PASSWORD_EDGE_MAX = 100;
	public static final String GMAIL_DECISION_ACCEPT = "accept";
	public static final String GMAIL_DECISION_DISMISS = "dismiss";
	public static final int GMAIL_ID_EDGE_MAX = 64;
	public static final int GMAIL_REDIRECT_EDGE_MAX = 300;
	public static final int GMAIL_CODE_EDGE_MAX = 2000;
	public static final int GMAIL_CLIENT_EDGE_MAX = 200;
	public static final int GMAIL_DAYS_MIN = 7;
	public static final int GMAIL_DAYS_MAX = 730;
	public static final int GMAIL_MAILS_MIN = 1;
	public static final int GMAIL_MAILS_MAX = 100;
	public static final String LOCAL_HTTP_PREFIX = "http://localhost";
	public static final String HTTPS_PREFIX = "https://";

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
