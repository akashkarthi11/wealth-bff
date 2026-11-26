package com.wealth.bff.contract;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.menu.NavMenuDto;

// The navigation for the signed-in user, built in the BSS from the table wealth_menus.
@RequestMapping(WealthConstants.MENUS_BASE)
public interface IfMenusContract {

	@PostMapping(WealthConstants.MENUS_LIST)
	ResponseEntity<ApiResponse<List<NavMenuDto>, Void>> list(@RequestHeader(WealthConstants.HEADER_AUTHORIZATION) String authorization)
			throws KarthiException;
}
