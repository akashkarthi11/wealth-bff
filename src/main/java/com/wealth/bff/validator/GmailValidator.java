package com.wealth.bff.validator;

import java.util.regex.Pattern;

import com.wealth.bff.constants.WealthConstants;
import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.GmailAppSaveRequestDto;
import com.wealth.common.model.dto.admin.GmailConnectRequestDto;
import com.wealth.common.model.dto.admin.GmailFinishRequestDto;
import com.wealth.common.model.dto.admin.GmailReviewRequestDto;
import com.wealth.common.model.dto.admin.GmailStatementSettingsRequestDto;
import com.wealth.common.model.dto.admin.StatementDownloadRequestDto;
import com.wealth.common.model.dto.admin.StatementRetryRequestDto;
import com.wealth.common.model.dto.admin.StatementUploadRequestDto;

// Edge checks only: shapes and lengths. The BSS checks the redirect path, the state, the code and who may change the Google client.
public final class GmailValidator {

	private static final Pattern ID_SHAPE = Pattern.compile("^[A-Za-z0-9-]+$");
	private static final Pattern PAN_SHAPE = Pattern.compile("^[A-Za-z]{5}[0-9]{4}[A-Za-z]$");
	private static final Pattern NAME_SHAPE = Pattern.compile("^[A-Za-z]{4}$");
	private static final Pattern DOB_SHAPE = Pattern.compile("^[0-9]{8}$");

	private GmailValidator() {
	}

	public static void validateConnect(GmailConnectRequestDto request) {
		if (request == null || !validRedirect(request.getRedirectUri())) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	public static void validateFinish(GmailFinishRequestDto request) {
		if (request == null || !validRedirect(request.getRedirectUri()) || blankOrLong(request.getCode(), WealthConstants.GMAIL_CODE_EDGE_MAX)
				|| blankOrLong(request.getState(), WealthConstants.GMAIL_CODE_EDGE_MAX)) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	public static void validateApp(GmailAppSaveRequestDto request) {
		if (request == null || (request.getClientId() != null && request.getClientId().length() > WealthConstants.GMAIL_CLIENT_EDGE_MAX)
				|| (request.getClientSecret() != null && request.getClientSecret().length() > WealthConstants.GMAIL_CLIENT_EDGE_MAX)
				|| outside(request.getStatementDays(), WealthConstants.GMAIL_DAYS_MIN, WealthConstants.GMAIL_DAYS_MAX)
				|| outside(request.getAlertDays(), WealthConstants.GMAIL_DAYS_MIN, WealthConstants.GMAIL_DAYS_MAX)
				|| outside(request.getStatementMax(), WealthConstants.GMAIL_MAILS_MIN, WealthConstants.GMAIL_MAILS_MAX)) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// The id of a pending transaction (letters, digits and dashes) and one of the two decisions.
	public static void validateReview(GmailReviewRequestDto request) {
		if (request == null || request.getId() == null || request.getId().length() > WealthConstants.GMAIL_ID_EDGE_MAX || !ID_SHAPE.matcher(request.getId()).matches()
				|| !(WealthConstants.GMAIL_DECISION_ACCEPT.equals(request.getDecision()) || WealthConstants.GMAIL_DECISION_DISMISS.equals(request.getDecision()))) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// The statement passwords: a PAN (ABCDE1234F) and a date of birth (DDMMYYYY), each optional (blank keeps the saved one). The values are never put in the error.
	public static void validateStatementSettings(GmailStatementSettingsRequestDto request) {
		if (request == null || !blankOrMatches(request.getPan(), PAN_SHAPE) || !blankOrMatches(request.getDob(), DOB_SHAPE) || !blankOrMatches(request.getName4(), NAME_SHAPE)) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// A PDF the user chose: a .pdf name, the file as base64 and an optional password; the BSS checks the size, the PDF header and the password length again.
	public static void validateStatementUpload(StatementUploadRequestDto request) {
		if (request == null || blankOrLong(request.getFileName(), WealthConstants.GMAIL_FILE_NAME_EDGE_MAX) || !request.getFileName().toLowerCase(java.util.Locale.ROOT).endsWith(".pdf")
				|| blankOrLong(request.getData(), WealthConstants.GMAIL_UPLOAD_DATA_EDGE_MAX) || (request.getPassword() != null && request.getPassword().length() > WealthConstants.GMAIL_PASSWORD_EDGE_MAX)) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// The id of one of the user's statements (letters, digits and dashes).
	public static void validateStatementDownload(StatementDownloadRequestDto request) {
		if (request == null || blankOrLong(request.getId(), WealthConstants.GMAIL_ID_EDGE_MAX) || !ID_SHAPE.matcher(request.getId()).matches()) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// A locked statement's id and the password typed for it; the value is never put in the error.
	public static void validateStatementRetry(StatementRetryRequestDto request) {
		if (request == null || blankOrLong(request.getId(), WealthConstants.GMAIL_ID_EDGE_MAX) || !ID_SHAPE.matcher(request.getId()).matches() || blankOrLong(request.getPassword(), WealthConstants.GMAIL_PASSWORD_EDGE_MAX)) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	// The statement whose rows are accepted: its id only.
	public static void validateStatementAccept(com.wealth.common.model.dto.admin.StatementAcceptRequestDto request) {
		if (request == null || blankOrLong(request.getId(), WealthConstants.GMAIL_ID_EDGE_MAX) || !ID_SHAPE.matcher(request.getId()).matches()) {
			throw new KarthiException(BaseError.GMAIL_REQUEST_INVALID);
		}
	}

	private static boolean blankOrMatches(String text, Pattern shape) {
		return text == null || text.isBlank() || shape.matcher(text).matches();
	}

	// https, or http on localhost for a local run; no spaces or control characters.
	private static boolean validRedirect(String uri) {
		return uri != null && uri.length() <= WealthConstants.GMAIL_REDIRECT_EDGE_MAX
				&& (uri.startsWith(WealthConstants.HTTPS_PREFIX) || uri.startsWith(WealthConstants.LOCAL_HTTP_PREFIX)) && uri.chars().noneMatch(c -> c <= ' ' || c == 127);
	}

	// A number that is sent must be inside its limits; not sent keeps the saved one.
	private static boolean outside(Integer value, int min, int max) {
		return value != null && (value < min || value > max);
	}

	private static boolean blankOrLong(String text, int max) {
		return text == null || text.isBlank() || text.length() > max;
	}
}
