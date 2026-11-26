package com.wealth.bff.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.wealth.common.generic.BaseError;
import com.wealth.common.generic.KarthiException;
import com.wealth.common.model.dto.admin.ConnectionRequestDto;

class ConnectionsValidatorTest {

	private ConnectionRequestDto request(String url) {
		ConnectionRequestDto r = new ConnectionRequestDto();
		r.setUrl(url);
		return r;
	}

	@Test
	void anOriginIsCleanedAndAccepted() {
		assertEquals("http://172.27.99.226:8081", ConnectionsValidator.validateUrl(request("  http://172.27.99.226:8081/ ")));
		assertEquals("https://example.com", ConnectionsValidator.validateUrl(request("HTTPS://Example.com")));
		assertEquals("http://localhost:8081", ConnectionsValidator.validateUrl(request("http://localhost:8081")));
	}

	@Test
	void everythingElseIsRefused() {
		for (String bad : new String[] { null, "", "   ", "ftp://host:21", "file:///etc/passwd", "javascript:alert(1)", "172.27.99.226:8081",
				"http://", "http://host/path", "http://host?x=1", "http://host#frag", "http://user:pw@host", "http://169.254.169.254", "http://bad host",
				"http://" + "a".repeat(250) }) {
			assertEquals(BaseError.CONNECTION_ADDRESS_INVALID,
					assertThrows(KarthiException.class, () -> ConnectionsValidator.validateUrl(request(bad))).getMessage(), String.valueOf(bad));
		}
		assertThrows(KarthiException.class, () -> ConnectionsValidator.validateUrl(null));
	}
}
