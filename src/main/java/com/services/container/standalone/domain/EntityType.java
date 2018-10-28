/**
 * 
 */
package com.services.container.standalone.domain;

import com.services.container.standalone.annotations.Component;
import com.services.container.standalone.annotations.Configuration;
import com.services.container.standalone.annotations.Entity;
import com.services.container.standalone.annotations.Inject;
import com.services.container.standalone.annotations.Service;

/**
 * Entity Type describing which kind of entity has been registered
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public enum EntityType {
	/**
	 * Component entity type
	 * @see Component
	 */
	COMPONENT, 
	/**
	 * Component entity from a object method call
	 * @see Component
	 */
	METHOD_COMPONENT, 
	/**
	 * Service entity type
	 * @see Service
	 */
	SERVICE, 
	/**
	 * Service entity from a object method call
	 * @see Service
	 */
	METHOD_SERVICE,
	/**
	 * Configuration parameters as Type annotation
	 * @see Configuration
	 */
	CONFIGURATION,
	/**
	 * Row entity as Type annotation
	 * @see Entity
	 */
	ENTITY, 
	/**
	 * Row entity from a object method call
	 * @see Service
	 */
	METHOD_ENTITY, 
	/**
	 * Method parameter row entity used in an object method call
	 * @see Inject
	 */
	METHOD_INJECTION, 
	/**
	 * Type Constructor parameter row entity used in an object constructor call
	 * @see Inject
	 */
	TYPE_INJECTION;

}
