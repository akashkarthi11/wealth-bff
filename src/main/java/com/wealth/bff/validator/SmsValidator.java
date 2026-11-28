package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.IdRequestDto;
import com.wealth.common.model.dto.sms.SmsDeviceCreateRequestDto;
import com.wealth.common.model.dto.sms.SmsMessageDto;
import com.wealth.common.model.dto.sms.SmsPushRequestDto;

// Edge checks only: field lengths and a bounded list. The BSS checks the token, reads the messages and decides what each one is.
public final class SmsValidator {

	private SmsValidator() {
	}

	public static void validateCreate(SmsDeviceCreateRequestDto request) {
		if (request == null || request.getLabel() == null || request.getLabel().length() > WealthConstants.SMS_LABEL_MAX_LENGTH * 2) {
			throw new KarthiException(BaseError.SMS_LABEL_INVALID);
		}
	}

	public static void validateId(IdRequestDto request) {
		if (request == null || request.getId() == null || request.getId().length() != WealthConstants.USAGE_USER_ID_LENGTH) {
			throw new KarthiException(BaseError.SMS_DEVICE_NOT_FOUND);
		}
	}

	// A wrong token shape gets the same refusal as an unknown token.
	public static void validateToken(String token) {
		if (token == null || token.length() > WealthConstants.SMS_TOKEN_MAX_LENGTH || !token.matches("[A-Za-z0-9_-]+")) {
			throw new KarthiException(BaseError.SMS_TOKEN_INVALID);
		}
	}

	public static void validatePush(SmsPushRequestDto request) {
		validateToken(request == null ? null : request.getToken());
		if (request.getMessages() == null || request.getMessages().isEmpty() || request.getMessages().size() > WealthConstants.SMS_PUSH_MAX) {
			throw new KarthiException(BaseError.SMS_PUSH_INVALID);
		}
		for (SmsMessageDto message : request.getMessages()) {
			if (message == null || (message.getSender() != null && message.getSender().length() > WealthConstants.SMS_SENDER_MAX_LENGTH)
					|| (message.getBody() != null && message.getBody().length() > WealthConstants.SMS_BODY_MAX_LENGTH)) {
				throw new KarthiException(BaseError.SMS_PUSH_INVALID);
			}
		}
	}
}
