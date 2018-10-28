/**
 * 
 */
package com.services.container.standalone.model;

import com.services.container.standalone.exceptions.ServiceInvocationException;

/**
 * Interface that declare the default container service features
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface IContainerService {
	void run() throws ServiceInvocationException;
}
