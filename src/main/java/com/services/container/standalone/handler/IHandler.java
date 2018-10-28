/**
 * 
 */
package com.services.container.standalone.handler;

import com.services.container.standalone.exceptions.HandlerInvocationException;

/**
 * Interface defining behaviours for handling changes on 
 * entity instances 
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface IHandler<T> {

	/**
	 * Method that operates change and return changed or new closed object
	 * @param source input element instance
	 * @return modified Object instance
	 * @throws HandlerInvocationException Arisen when eny exception occurs during transformation
	 */
	T handle(T source) throws HandlerInvocationException;
}
