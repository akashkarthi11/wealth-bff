package com.wealth.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.wealth.bff.client.WealthFeignClient;
import com.wealth.bff.contract.IfMenusContract;
import com.wealth.common.generic.ApiResponse;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.logger.KarthiLogger;
import com.wealth.common.model.dto.menu.NavMenuDto;
import com.wealth.common.utils.generic.RequestHeaderUtil;

@RestController
public class MenusController extends WealthBaseController implements IfMenusContract {

	private static final KarthiLogger LOGGER = new KarthiLogger(MenusController.class);

	private final WealthFeignClient feignClient;

	public MenusController(WealthFeignClient feignClient) {
		this.feignClient = feignClient;
	}

	@Override
	public ResponseEntity<ApiResponse<List<NavMenuDto>, Void>> list(String authorization) throws KarthiException {
		LOGGER.info("Entering list BFF");
		return handleResponse(feignClient.menusList(authorization, RequestHeaderUtil.getIp()));
	}
}
