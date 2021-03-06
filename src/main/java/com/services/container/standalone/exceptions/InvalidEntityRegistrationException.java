/**
 * 
 */
package com.services.container.standalone.exceptions;

/**
 * Exception arisen when an error occurs during entity registration
 * in the system annotation load and registration into the singleton
 * registry memory allocation
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class InvalidEntityRegistrationException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = 5861645661483225877L;

	/**
	 * Default constructor
	 */
	public InvalidEntityRegistrationException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public InvalidEntityRegistrationException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public InvalidEntityRegistrationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public InvalidEntityRegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report Serializable stack trace
	 */
	public InvalidEntityRegistrationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
