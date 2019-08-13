package com.rxsavings.location.exception;

public class LocationException extends RuntimeException {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 7862995739703992780L;

	/**
	 * Parameterized constructor of {@link LocationException}
	 * 
	 * @param message cause of exception
	 */
	public LocationException(final String message) {
		super(message);
	}

	/**
	 * Parameterized constructor to create {@link LocationException} with a cause.
	 * 
	 * @param cause Throwable Exception.
	 */
	public LocationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Parameterized constructor to create {@link LocationException} with a Message
	 * and cause.
	 * 
	 * @param message Cause of exception.
	 * @param cause   Throwable Exception.
	 */
	public LocationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
