/**
 * 
 */
package com.services.container.standalone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that describes a wired entity from the container, using java name notation from the parameter or the given name
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
public @interface Wired {
	/**
	 * Name of desired bean / entity
	 * @return Entity name
	 */
	public String entityName() default "";
}
