package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.RecordsRequestDto;
import com.wealth.common.model.dto.admin.RoleCreateRequestDto;
import com.wealth.common.model.dto.admin.RoleMenusDto;
import com.wealth.common.model.dto.admin.UserRoleRequestDto;

// Edge checks only: shape and length. Which menus exist, which role may change and who may call it are decided in the BSS.
public final class AdminValidator {

	private static final Pattern KEY = Pattern.compile("^[a-z0-9-]+$");
	private static final Pattern USER_ID = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

	private AdminValidator() {
	}

	public static void validateRoleMenus(RoleMenusDto request) {
		if (request == null || request.getRole() == null || request.getRole().isBlank()
				|| request.getRole().length() > WealthConstants.ROLE_MAX_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
		if (request.getMenus() == null || request.getMenus().size() > WealthConstants.MENUS_MAX) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
		for (String key : request.getMenus()) {
			if (key == null || key.length() > WealthConstants.MENU_KEY_MAX_LENGTH || !KEY.matcher(key).matches()) {
				throw new KarthiException(BaseError.INVALID_REQUEST);
			}
		}
	}

	// Edge checks only: a name that is not absurd. The BSS makes the key from it and decides whether it is valid or taken.
	public static void validateRoleCreate(RoleCreateRequestDto request) {
		if (request == null || request.getLabel() == null || request.getLabel().isBlank() || request.getLabel().length() > WealthConstants.ROLE_LABEL_EDGE_MAX) {
			throw new KarthiException(BaseError.ROLE_NAME_INVALID);
		}
	}

	public static void validateUserRole(UserRoleRequestDto request) {
		if (request == null || request.getUserId() == null || !USER_ID.matcher(request.getUserId()).matches() || request.getRole() == null
				|| request.getRole().length() > WealthConstants.ROLE_MAX_LENGTH || !KEY.matcher(request.getRole()).matches()) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateRecords(RecordsRequestDto request) {
		if (request != null && request.getQ() != null && request.getQ().length() > WealthConstants.SEARCH_MAX_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}
}
