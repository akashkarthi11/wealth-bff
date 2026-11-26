package com.wealth.bff.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.RoleMenusDto;

class AdminValidatorTest {

	private RoleMenusDto request(String role, List<String> menus) {
		RoleMenusDto r = new RoleMenusDto();
		r.setRole(role);
		r.setMenus(menus);
		return r;
	}

	@Test
	void aWellFormedRequestPasses() {
		assertDoesNotThrow(() -> AdminValidator.validateRoleMenus(request("user", List.of("overview", "stock-advisor"))));
	}

	@Test
	void badShapesAreRefused() {
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(null));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request(null, List.of("overview"))));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request(" ", List.of("overview"))));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request("user", null)));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request("user", Arrays.asList("overview", null))));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request("user", List.of("Over View"))));
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request("user", List.of("a'; drop table"))));
	}

	@Test
	void tooManyMenusOrTooLongAKeyIsRefused() {
		List<String> many = new ArrayList<>();
		for (int i = 0; i <= WealthConstants.MENUS_MAX; i++) {
			many.add("m" + i);
		}
		assertThrows(KarthiException.class, () -> AdminValidator.validateRoleMenus(request("user", many)));
		assertThrows(KarthiException.class,
				() -> AdminValidator.validateRoleMenus(request("user", List.of("a".repeat(WealthConstants.MENU_KEY_MAX_LENGTH + 1)))));
	}
}
