/**
 * 
 */
package com.services.container.standalone.exceptions;

/**
 * Exception arisen when user try to run the container and scan the code twice
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class StandAloneContainerAlreadyRunningException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = 5861645661483225877L;

	/**
	 * Default constructor
	 */
	public StandAloneContainerAlreadyRunningException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public StandAloneContainerAlreadyRunningException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public StandAloneContainerAlreadyRunningException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public StandAloneContainerAlreadyRunningException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report Serializable stack trace
	 */
	public StandAloneContainerAlreadyRunningException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
