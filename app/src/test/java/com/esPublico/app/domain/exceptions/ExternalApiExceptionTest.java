package com.esPublico.app.domain.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExternalApiExceptionTest {

	@Test
	public void testExternalApiExceptionWithMessageAndCause() {

		final String message = "Error calling external API";
		final Throwable cause = new RuntimeException("cause");

		final ExternalApiException exception = new ExternalApiException(message, cause);

		assertEquals(message, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}

	@Test
	public void testExternalApiExceptionWithoutCause() {

		final String message = "Error calling external API";

		final ExternalApiException exception = new ExternalApiException(message, null);

		assertEquals(message, exception.getMessage());
		assertNull(exception.getCause());
	}
}
