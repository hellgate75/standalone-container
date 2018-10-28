/**
 * 
 */
package com.services.container.standalone.exceptions;

import com.services.container.standalone.annotations.Execute;
import com.services.container.standalone.annotations.Service;
import com.services.container.standalone.model.IContainerService;

/**
 * Exception arisen when user try to run the service of type {@link IContainerService}
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 * @see IContainerService
 * @see Execute
 * @see Service
 */
public class ServiceExecutionException extends Exception {

	/**
	 * Exception unique identifier for Java serialization
	 */
	private static final long serialVersionUID = -1607815348989336352L;

	/**
	 * Default constructor
	 */
	public ServiceExecutionException() {
		super();
	}

	/**
	 * Constructor reporting error message
	 * @param message Error message
	 */
	public ServiceExecutionException(String message) {
		super(message);
	}

	/**
	 * Constructor reporting error exception cause
	 * @param cause Exception cause
	 */
	public ServiceExecutionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor reporting error message and cause
	 * @param message Error message
	 * @param cause Exception cause
	 */
	public ServiceExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor reporting error message and cause and stack of error
	 * @param message Error message
	 * @param cause Exception cause
	 * @param enableSuppression Suppress error reporting stack
	 * @param writableStackTrace report Serializable stack trace
	 */
	public ServiceExecutionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
