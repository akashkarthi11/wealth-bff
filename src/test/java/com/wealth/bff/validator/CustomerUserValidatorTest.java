package com.wealth.bff.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.customer.LoginRequestDto;
import com.wealth.common.model.dto.customer.PasswordResetRequestDto;
import com.wealth.common.model.dto.customer.RegisterRequestDto;

class CustomerUserValidatorTest {

	private LoginRequestDto login(String email, String password) {
		LoginRequestDto r = new LoginRequestDto();
		r.setEmail(email);
		r.setPassword(password);
		return r;
	}

	private String code(Runnable call) {
		return assertThrows(KarthiException.class, call::run).getMessage();
	}

	@Test
	void aWellFormedLoginPasses() {
		assertDoesNotThrow(() -> CustomerUserValidator.validateLogin(login("owner@example.com", "anything")));
	}

	@Test
	void missingBodyEmailAndPasswordHaveTheirOwnCodes() {
		assertEquals(BaseError.INVALID_REQUEST, code(() -> CustomerUserValidator.validateLogin(null)));
		assertEquals(BaseError.EMAIL_CANNOT_BE_NULL_OR_EMPTY, code(() -> CustomerUserValidator.validateLogin(login(" ", "x"))));
		assertEquals(BaseError.INVALID_EMAIL_FORMAT, code(() -> CustomerUserValidator.validateLogin(login("no-at-sign", "x"))));
		assertEquals(BaseError.INVALID_EMAIL_FORMAT,
				code(() -> CustomerUserValidator.validateLogin(login("a".repeat(250) + "@b.co", "x"))));
		assertEquals(BaseError.PASSWORD_EMPTY, code(() -> CustomerUserValidator.validateLogin(login("a@b.co", ""))));
		assertEquals(BaseError.PASSWORD_TOO_LONG,
				code(() -> CustomerUserValidator.validateLogin(login("a@b.co", "x".repeat(201)))));
	}

	@Test
	void registerAndResetUseTheSameRules() {
		RegisterRequestDto reg = new RegisterRequestDto();
		reg.setEmail("bad");
		reg.setPassword("x");
		assertEquals(BaseError.INVALID_EMAIL_FORMAT, code(() -> CustomerUserValidator.validateRegister(reg)));
		PasswordResetRequestDto reset = new PasswordResetRequestDto();
		reset.setEmail("a@b.co");
		assertEquals(BaseError.PASSWORD_EMPTY, code(() -> CustomerUserValidator.validatePasswordReset(reset)));
		assertEquals(BaseError.INVALID_REQUEST, code(() -> CustomerUserValidator.validatePasswordReset(null)));
	}
}
