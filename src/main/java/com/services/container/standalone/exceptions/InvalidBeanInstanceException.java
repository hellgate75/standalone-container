/**
 * 
 */
package com.services.container.standalone.exceptions;

/**
 * Exception arisen when an error occurs during entity registry
 * recovery and instance or call application
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class InvalidBeanInstanceException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = -8637437592038461516L;

	/**
	 * Default constructor
	 */
	public InvalidBeanInstanceException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public InvalidBeanInstanceException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public InvalidBeanInstanceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public InvalidBeanInstanceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report Serializable stack trace
	 */
	public InvalidBeanInstanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
