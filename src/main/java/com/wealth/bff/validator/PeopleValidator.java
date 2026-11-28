package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.SplitCreateRequestDto;
import com.wealth.common.model.dto.people.SplitPersonRequestDto;

// Edge checks only: a transaction id, a short mode, a bounded list and field lengths. The BSS checks the transaction, the people and the amounts.
public final class PeopleValidator {

	private PeopleValidator() {
	}

	public static void validateSplit(SplitCreateRequestDto request) {
		if (request == null || request.getTransactionId() == null || request.getTransactionId().length() != WealthConstants.USAGE_USER_ID_LENGTH || request.getMode() == null
				|| request.getMode().length() > WealthConstants.PEOPLE_MODE_MAX_LENGTH || tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2) || request.getPeople() == null
				|| request.getPeople().isEmpty() || request.getPeople().size() > WealthConstants.PEOPLE_SPLIT_MAX_PEOPLE) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
		for (SplitPersonRequestDto person : request.getPeople()) {
			if (person == null || tooLong(person.getPersonId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(person.getName(), WealthConstants.PEOPLE_NAME_MAX_LENGTH * 2)) {
				throw new KarthiException(BaseError.INVALID_REQUEST);
			}
		}
	}

	private static boolean tooLong(String value, int max) {
		return value != null && value.length() > max;
	}
}
