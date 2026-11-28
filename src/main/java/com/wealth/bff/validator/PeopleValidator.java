package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.people.CommentCreateRequestDto;
import com.wealth.common.model.dto.people.EntryNoteRequestDto;
import com.wealth.common.model.dto.people.LendPaidRequestDto;
import com.wealth.common.model.dto.people.LendRemarkRequestDto;
import com.wealth.common.model.dto.people.RequestDecideRequestDto;
import com.wealth.common.model.dto.people.PersonEntryRequestDto;
import com.wealth.common.model.dto.people.PersonMergeRequestDto;
import com.wealth.common.model.dto.people.LendViewRequestDto;
import com.wealth.common.model.dto.people.ShareUpdateRequestDto;
import com.wealth.common.model.dto.people.PersonSaveRequestDto;
import com.wealth.common.model.dto.people.RecordStatusRequestDto;
import com.wealth.common.model.dto.people.RoundOffRequestDto;
import com.wealth.common.model.dto.people.SharePaidRequestDto;
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

	// Edge checks: id shape and length, a bounded id list, short text and a short direction. The BSS checks the transfers, the person and the amounts.
	public static void validateEntry(PersonEntryRequestDto request) {
		if (request == null || tooLong(request.getPersonId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getName(), WealthConstants.PEOPLE_NAME_MAX_LENGTH * 2)
				|| tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2) || tooLong(request.getDirection(), WealthConstants.PEOPLE_DIRECTION_MAX_LENGTH)
				|| tooLong(request.getOccurredOn(), WealthConstants.PEOPLE_DAY_LENGTH) || request.getTransactionIds() == null
				|| request.getTransactionIds().size() > WealthConstants.PEOPLE_ENTRY_MAX_TRANSACTIONS) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
		for (String id : request.getTransactionIds()) {
			if (id == null || id.length() != WealthConstants.USAGE_USER_ID_LENGTH) {
				throw new KarthiException(BaseError.INVALID_REQUEST);
			}
		}
	}

	public static void validateSharePaid(SharePaidRequestDto request) {
		if (request == null || request.getSplitId() == null || request.getSplitId().length() != WealthConstants.USAGE_USER_ID_LENGTH || request.getPersonId() == null
				|| request.getPersonId().length() != WealthConstants.USAGE_USER_ID_LENGTH || tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)
				|| tooLong(request.getOccurredOn(), WealthConstants.PEOPLE_DAY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateRoundOff(RoundOffRequestDto request) {
		if (request == null || request.getPersonId() == null || request.getPersonId().length() != WealthConstants.USAGE_USER_ID_LENGTH || tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)
				|| tooLong(request.getClosedOn(), WealthConstants.PEOPLE_DAY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateRecord(RecordStatusRequestDto request) {
		if (request == null || request.getStatus() == null || request.getStatus().length() > WealthConstants.PEOPLE_STATUS_MAX_LENGTH || tooLong(request.getEntryId(), WealthConstants.USAGE_USER_ID_LENGTH)
				|| tooLong(request.getSplitId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getPersonId(), WealthConstants.USAGE_USER_ID_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateSave(PersonSaveRequestDto request) {
		if (request == null || tooLong(request.getPersonId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getName(), WealthConstants.PEOPLE_NAME_MAX_LENGTH * 2)
				|| tooLong(request.getKind(), WealthConstants.PEOPLE_STATUS_MAX_LENGTH) || tooLong(request.getStatus(), WealthConstants.PEOPLE_STATUS_MAX_LENGTH)
				|| tooLong(request.getDueOn(), WealthConstants.PEOPLE_DAY_LENGTH) || tooLong(request.getAnnualRate(), WealthConstants.PEOPLE_RATE_MAX_LENGTH)
				|| tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateShare(ShareUpdateRequestDto request) {
		if (request == null || request.getEnabled() == null || request.getPersonId() == null || request.getPersonId().length() != WealthConstants.USAGE_USER_ID_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	// The public page: the secret is 32 to 64 URL-safe characters or nothing is forwarded. It is never logged here.
	public static void validateLendView(LendViewRequestDto request) {
		String token = request == null ? null : request.getToken();
		if (token == null || token.length() < WealthConstants.SHARE_TOKEN_MIN_LENGTH || token.length() > WealthConstants.SHARE_TOKEN_MAX_LENGTH || !token.matches("[A-Za-z0-9_-]+")) {
			throw new KarthiException(BaseError.SHARE_LINK_INVALID);
		}
	}

	public static void validateMerge(PersonMergeRequestDto request) {
		if (request == null || request.getFromId() == null || request.getFromId().length() != WealthConstants.USAGE_USER_ID_LENGTH || request.getIntoId() == null
				|| request.getIntoId().length() != WealthConstants.USAGE_USER_ID_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static boolean secretOk(String token) {
		return token != null && token.length() >= WealthConstants.SHARE_TOKEN_MIN_LENGTH && token.length() <= WealthConstants.SHARE_TOKEN_MAX_LENGTH && token.matches("[A-Za-z0-9_-]+");
	}

	public static void validateComment(CommentCreateRequestDto request) {
		if (request == null || tooLong(request.getEntryId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getShareId(), WealthConstants.USAGE_USER_ID_LENGTH)
				|| tooLong(request.getBody(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateNote(EntryNoteRequestDto request) {
		if (request == null || request.getEntryId() == null || request.getEntryId().length() != WealthConstants.USAGE_USER_ID_LENGTH || tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateDecide(RequestDecideRequestDto request) {
		if (request == null || request.getRequestId() == null || request.getRequestId().length() != WealthConstants.USAGE_USER_ID_LENGTH || request.getDecision() == null
				|| request.getDecision().length() > WealthConstants.PEOPLE_DECISION_MAX_LENGTH || tooLong(request.getPaidOn(), WealthConstants.PEOPLE_DAY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	// The public page's messages: the secret must look real, or nothing is forwarded.
	public static void validateLendPaid(LendPaidRequestDto request) {
		if (request == null || !secretOk(request.getToken())) {
			throw new KarthiException(BaseError.SHARE_LINK_INVALID);
		}
		if (tooLong(request.getPaidOn(), WealthConstants.PEOPLE_DAY_LENGTH) || tooLong(request.getMethod(), WealthConstants.PEOPLE_DECISION_MAX_LENGTH)
				|| tooLong(request.getNote(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateLendRemark(LendRemarkRequestDto request) {
		if (request == null || !secretOk(request.getToken())) {
			throw new KarthiException(BaseError.SHARE_LINK_INVALID);
		}
		if (tooLong(request.getEntryId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getShareId(), WealthConstants.USAGE_USER_ID_LENGTH)
				|| tooLong(request.getBody(), WealthConstants.PEOPLE_NOTE_MAX_LENGTH * 2)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static boolean tooLong(String value, int max) {
		return value != null && value.length() > max;
	}
}
