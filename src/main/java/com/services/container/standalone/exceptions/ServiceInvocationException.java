/**
 * 
 */
package com.services.container.standalone.exceptions;

import java.io.Serializable;

/**
 * Exception arisen when an error occurs during service execution
 * invocation
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class ServiceInvocationException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = 8427094348348353511L;

	/**
	 * Default constructor
	 */
	public ServiceInvocationException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public ServiceInvocationException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public ServiceInvocationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public ServiceInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report {@link Serializable} stack trace
	 */
	public ServiceInvocationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
