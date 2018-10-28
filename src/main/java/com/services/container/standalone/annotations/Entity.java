/**
 * 
 */
package com.services.container.standalone.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation describes the configuration parameters. It's not needed for scan components,
 * however it describe for instance packages to scan. If no package is defined engine will scann all classes, despite performance
 * of the coded application
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface Entity {
	/**
	 * Name of declared bean / entity
	 * @return Component name
	 */
	public String name() default "";
}
