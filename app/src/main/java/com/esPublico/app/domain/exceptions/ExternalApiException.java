package com.esPublico.app.domain.exceptions;

public class ExternalApiException extends RuntimeException {
	public ExternalApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
