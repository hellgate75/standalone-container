/**
 * 
 */
package com.services.container.standalone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface Service {
	/**
	 * Name of declared bean / entity
	 * @return Service name
	 */
	public String name() default "";
}
