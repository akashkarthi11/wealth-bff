package com.wealth.bff.validator;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.transaction.TransactionCreateRequestDto;
import com.wealth.common.model.dto.transaction.TransactionPageRequestDto;
import com.wealth.common.model.dto.transaction.WorkflowDecisionRequestDto;

// Edge checks only: sizes and lengths. The BSS checks the kinds, dates, accounts and rules.
public final class TransactionsValidator {

	private TransactionsValidator() {
	}

	public static void validatePage(TransactionPageRequestDto request) {
		if (request == null) {
			return;
		}
		if ((request.getSize() != null && (request.getSize() < 1 || request.getSize() > WealthConstants.TRANSACTION_PAGE_MAX)) || tooLong(request.getQ(), WealthConstants.TRANSACTION_SEARCH_MAX_LENGTH)
				|| tooLong(request.getCursor(), WealthConstants.TRANSACTION_CURSOR_MAX_LENGTH) || tooLong(request.getKind(), WealthConstants.TRANSACTION_KIND_MAX_LENGTH)
				|| tooLong(request.getAccountId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getFrom(), WealthConstants.TRANSACTION_DAY_LENGTH)
				|| tooLong(request.getTo(), WealthConstants.TRANSACTION_DAY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	public static void validateCreate(TransactionCreateRequestDto request) {
		if (request == null || request.getAmount() == null || tooLong(request.getKind(), WealthConstants.TRANSACTION_KIND_MAX_LENGTH) || tooLong(request.getCategory(), WealthConstants.TRANSACTION_CATEGORY_MAX_LENGTH * 2)
				|| tooLong(request.getDescription(), WealthConstants.TRANSACTION_DESCRIPTION_MAX_LENGTH * 2) || tooLong(request.getExternalRef(), WealthConstants.TRANSACTION_REFERENCE_MAX_LENGTH * 2)
				|| tooLong(request.getAccountId(), WealthConstants.USAGE_USER_ID_LENGTH) || tooLong(request.getAccount(), WealthConstants.ACCOUNT_TEXT_MAX_LENGTH * 2)
				|| tooLong(request.getDate(), WealthConstants.TRANSACTION_DAY_LENGTH)) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	// Edge check of a decision: the line's id and a short decision word. The BSS checks the line, who it belongs to and whether it can be decided.
	public static void validateDecision(WorkflowDecisionRequestDto request) {
		if (request == null || request.getId() == null || request.getId().length() != WealthConstants.USAGE_USER_ID_LENGTH || request.getDecision() == null
				|| request.getDecision().isBlank() || request.getDecision().length() > WealthConstants.WORKFLOW_DECISION_MAX_LENGTH) {
			throw new KarthiException(BaseError.INVALID_REQUEST);
		}
	}

	private static boolean tooLong(String value, int max) {
		return value != null && value.length() > max;
	}
}
