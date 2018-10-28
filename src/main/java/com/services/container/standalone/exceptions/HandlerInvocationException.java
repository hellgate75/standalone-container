/**
 * 
 */
package com.services.container.standalone.exceptions;

/**
 * Exception arisen when an error occurs during entity handling
 * invocation
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class HandlerInvocationException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = -8637437592038461516L;

	/**
	 * Default constructor
	 */
	public HandlerInvocationException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public HandlerInvocationException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public HandlerInvocationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public HandlerInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report Serializable stack trace
	 */
	public HandlerInvocationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
